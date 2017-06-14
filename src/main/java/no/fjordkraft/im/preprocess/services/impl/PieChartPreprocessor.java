package no.fjordkraft.im.preprocess.services.impl;

import no.fjordkraft.im.if320.models.DistributionDetails;
import no.fjordkraft.im.if320.models.PieChart;
import no.fjordkraft.im.if320.models.Statement;
import no.fjordkraft.im.if320.models.Transaction;
import no.fjordkraft.im.preprocess.models.PreprocessRequest;
import no.fjordkraft.im.preprocess.models.PreprocessorInfo;
import no.fjordkraft.im.util.IMConstants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miles on 5/23/2017.
 */
@Service
@PreprocessorInfo(order=8)
public class PieChartPreprocessor extends BasePreprocessor {

    @Override
    public void preprocess(PreprocessRequest<Statement, no.fjordkraft.im.model.Statement> request) {

        List<Transaction> transactions = request.getStatement().getTransactionGroup().getTransaction();
        List<DistributionDetails> distributionList = new ArrayList<DistributionDetails>();
        DistributionDetails distributionDetails = new DistributionDetails();
        PieChart pieChart = new PieChart();
        float nettAmount = 0;
        float otherAmount = 0;

        for(Transaction transaction:transactions) {
            if (null != transaction.getTransactionType()) {
                if (IMConstants.NETT.equals(transaction.getTransactionType())) {
                    nettAmount += transaction.getAmountWithVat();
                } else {
                    otherAmount += transaction.getAmountWithVat();
                }
            }
        }
        distributionDetails.setType(IMConstants.NETTLEIE);
        distributionDetails.setAmount(nettAmount);
        distributionList.add(distributionDetails);

        distributionDetails = new DistributionDetails();
        distributionDetails.setType(IMConstants.STROM);
        distributionDetails.setAmount(otherAmount);
        distributionList.add(distributionDetails);

        pieChart.setDistribution(distributionList);

        request.getStatement().setPieChart(pieChart);
    }
}
