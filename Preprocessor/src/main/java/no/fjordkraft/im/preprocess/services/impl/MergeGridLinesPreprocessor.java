package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.*;
import no.fjordkraft.im.model.GridGroup;
import no.fjordkraft.im.model.GridLine;
import no.fjordkraft.im.model.GroupGridLine;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.services.GridGroupService;
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
@PreprocessorInfo(order = 13)
public class MergeGridLinesPreprocessor extends BasePreprocessor {
    private static final Logger logger = LoggerFactory.getLogger(MergeGridLinesPreprocessor.class);

     @Autowired
     GridGroupService gridGroupService;

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        Attachments attachments = request.getStatement().getAttachments();
        for (int i = 0; i < attachments.getAttachment().size(); i++)
        {
           Attachment attachment = attachments.getAttachment().get(i);
           Nettleie nettleie = attachment.getFAKTURA().getVEDLEGGEMUXML().getInvoice().getInvoiceFinalOrder().getNettleie();
           String gridName = null;
           if( attachment.getFAKTURA().getGrid()!=null )
               gridName = attachment.getFAKTURA().getGrid().getName().trim();
          if(nettleie!=null && nettleie.getGridName()!=null && gridName!=null && gridName.toUpperCase().contains(nettleie.getGridName().trim().toUpperCase()))
          {
              List<GridGroup> listOfGridGroups = gridGroupService.getGridGroupByGridConfigName(gridName);
              Map<String,List<String>> groupVsGridlines = new HashMap<>();
              for(GridGroup gridGroup :listOfGridGroups )
              {
                    List<String> gridLineNames = new ArrayList<String>();
                    for(GroupGridLine groupGridLine: gridGroup.getGroupGridLines() )
                    {
                        GridLine gridLine = groupGridLine.getGridLine();
                        gridLineNames.add(gridLine.getGridLineName());
                    }
                    groupVsGridlines.put(gridGroup.getGridGroupName(),gridLineNames);
                }
                List<BaseItemDetails> newBaseItemDetails = new ArrayList<BaseItemDetails>();
                for(String mergeGridName:groupVsGridlines.keySet())
                {
                   List<BaseItemDetails> mergedLineItems = new ArrayList<BaseItemDetails>();
                   List<String> toMergeLineItems = groupVsGridlines.get(mergeGridName);
                    for(BaseItemDetails itemDetail : nettleie.getBaseItemDetails())
                    {
                        if(toMergeLineItems!=null && !toMergeLineItems.isEmpty() && toMergeLineItems.contains(itemDetail.getDescription().trim()))
                        {
                            itemDetail.setDescription(mergeGridName);
                          if(mergedLineItems.isEmpty())
                              mergedLineItems.add(itemDetail);
                          else
                          {
                               for(BaseItemDetails mergeLineItem :mergedLineItems)
                               {
                                   mergeLineItem.setUnitPriceGross(mergeLineItem.getUnitPriceGross()+itemDetail.getUnitPriceGross());
                                   mergeLineItem.setLineItemGrossAmount(mergeLineItem.getLineItemGrossAmount()+itemDetail.getLineItemGrossAmount());
                               }
                          }
                        }
                        else
                            newBaseItemDetails.add(itemDetail);
                    }
                    newBaseItemDetails.addAll(mergedLineItems);
                    nettleie.setBaseItemDetails(newBaseItemDetails);
                }
            }
        }
    }
}
