package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.model.GridConfig;
import no.fjordkraft.im.model.GridGroup;
import no.fjordkraft.im.model.GridLine;
import no.fjordkraft.im.model.GroupGridLine;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.AuditLogService;
import no.fjordkraft.im.services.GridConfigService;
import no.fjordkraft.im.services.GridGroupService;
import no.fjordkraft.im.statusEnum.StatementStatusEnum;
import no.fjordkraft.im.util.IMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@PreprocessorInfo(order = 75)
public class MergeGridLinesPreprocessor extends BasePreprocessor {
    private static final Logger logger = LoggerFactory.getLogger(MergeGridLinesPreprocessor.class);

     @Autowired
     GridGroupService gridGroupService;

    @Autowired
    GridConfigService gridConfigService;

    @Autowired
    AuditLogService auditLogService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        Attachments attachments = request.getStatement().getAttachments();
        for (int i = 0; i < attachments.getAttachment().size(); i++)
        {
           Attachment attachment = attachments.getAttachment().get(i);
            if((attachment.getDisplayStromData()!=null && attachment.getDisplayStromData()) ||(attachment.isOnlyGrid())) {
           Nettleie nettleie = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie();
           String gridName = null;
           if( attachment.getFAKTURA().getGrid()!=null && attachment.getFAKTURA().getGrid().getName()!=null)
               gridName = attachment.getFAKTURA().getGrid().getName().trim();

             if(attachment.getFAKTURA().getVEDLEGGEMUXML()!=null  && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice()!=null && attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder()!=null &&
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderInfo110()!=null &&
                attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderInfo110().getLDC1()!=null)
                    gridName = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getInvoiceOrderInfo110().getLDC1();
            if(null!=nettleie) {
                if ((gridName == null || (gridName != null && gridName.isEmpty())) && nettleie.getGridName() != null)
                    gridName = nettleie.getGridName().trim();
                attachment.getFAKTURA().setGrid(getGridConfigInfo(gridName, request.getEntity().getId()));
                if (nettleie != null && nettleie.getGridName() != null && gridName != null && gridName.toUpperCase().contains(nettleie.getGridName().trim().toUpperCase())) {
                    List<GridGroup> listOfGridGroups = gridGroupService.getGridGroupByGridConfigName(gridName.toUpperCase());
                    Map<String, List<String>> groupVsGridlines = new HashMap<>();
                    for (GridGroup gridGroup : listOfGridGroups) {
                        logger.debug("Found group of grids " + listOfGridGroups.size() + " for Grid Name " + gridName);
                        List<String> gridLineNames = new ArrayList<String>();
                        for (GroupGridLine groupGridLine : gridGroup.getGroupGridLines()) {
                            GridLine gridLine = groupGridLine.getGridLine();
                            gridLineNames.add(gridLine.getGridLineName());
                        }
                        groupVsGridlines.put(gridGroup.getGridGroupName(), gridLineNames);
                    }
                    List<BaseItemDetails> newBaseItemDetails = new ArrayList<BaseItemDetails>();
                    for (String mergeGridName : groupVsGridlines.keySet()) {
                        List<BaseItemDetails> mergedLineItems = new ArrayList<BaseItemDetails>();
                        List<String> toMergeLineItems = groupVsGridlines.get(mergeGridName);
                        for (BaseItemDetails itemDetail : nettleie.getBaseItemDetails()) {
                            if (toMergeLineItems != null && !toMergeLineItems.isEmpty() && toMergeLineItems.contains(itemDetail.getDescription().trim())) {
                                String message = "Merging grid line " + itemDetail.getDescription().trim() + " into new grid name " + mergeGridName;
                                auditLogService.saveAuditLog(request.getEntity().getId(), StatementStatusEnum.PRE_PROCESSING.getStatus(), message, IMConstants.INFO,request.getEntity().getLegalPartClass());
                                logger.debug("Merging item details for " + itemDetail.getDescription() + " into grid name " + mergeGridName);
                                itemDetail.setDescription(mergeGridName);
                                if (mergedLineItems.isEmpty())
                                    mergedLineItems.add(itemDetail);
                                else {
                                    for (BaseItemDetails mergeLineItem : mergedLineItems) {
                                        mergeLineItem.setUnitPriceGross(mergeLineItem.getUnitPriceGross() + itemDetail.getUnitPriceGross());
                                        mergeLineItem.setLineItemGrossAmount(mergeLineItem.getLineItemGrossAmount() + itemDetail.getLineItemGrossAmount());
                                    }
                                }
                            } else
                                newBaseItemDetails.add(itemDetail);
                        }
                        newBaseItemDetails.addAll(mergedLineItems);
                        nettleie.setBaseItemDetails(newBaseItemDetails);
                    }
                }
            }
            }
        }

    }

    private Grid getGridConfigInfo(String ldc1, Long id) {
        Grid grid = new Grid();

        GridConfig gridConfig = gridConfigService.getGridConfigByBrand(ldc1.toUpperCase());
        if(null != gridConfig) {
            grid.setName(gridConfig.getGridLabel());
            grid.setEmail(gridConfig.getEmail());
            grid.setTelephone(gridConfig.getPhone());
        } else {
            String errorMessage = "Grid config not found "+ldc1.toUpperCase();
            auditLogService.saveAuditLog(id, StatementStatusEnum.PRE_PROCESSING.getStatus(), errorMessage, IMConstants.WARNING,null);
        }
        return grid;
    }

}
