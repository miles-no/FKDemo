package no.fjordkraft.afi.server.emuxml.jpa.domain;

import lombok.Data;

@Data
public class EmuXmlHandleRequest {

    private String emuXmlFilename;

    private boolean storeAttachmentFile;
    private boolean storeIF318File;
    private boolean storeEmuXmlDb;

    private long numTransactions; // Number of transactions in the EMU XML file

    public static EmuXmlHandleRequest parseAndStore(String emuXmlFilename, boolean storeFiles, boolean storeDatabase) {
        EmuXmlHandleRequest r = new EmuXmlHandleRequest();
        r.setEmuXmlFilename(emuXmlFilename);
        r.setStoreAttachmentFile(storeFiles);
        r.setStoreIF318File(storeFiles);
        r.setStoreEmuXmlDb(storeDatabase);
        return r;
    }

}
