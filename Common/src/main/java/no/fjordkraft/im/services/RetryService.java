package no.fjordkraft.im.services;

/**
 * Created by miles on 7/25/2017.
 */
public interface RetryService {

    void updateTransferfileStatus(String transfertype, String brand, String filename, String status);
}
