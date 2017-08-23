package no.fjordkraft.im.model;

public enum TransferStatusEnum {
    Created(1, "File has been exported", "Opprettet", TransferStatusTypeEnum.running),
    ReadyForScan(9, "File is ready for scanning", "Klar for scanning", TransferStatusTypeEnum.ok),
    Scanning(10, "File is being scanned", "Skannes..", TransferStatusTypeEnum.running),
    Scanned(11, "File has been scanned", "Skannet", TransferStatusTypeEnum.ok),
    Storing(12, "File is being stored in the database", "Lagres i databasen...", TransferStatusTypeEnum.running),
    Stored(13, "File has been stored in the database", "Lagret i databasen", TransferStatusTypeEnum.ok),
    ReadyForZip(20, "File is ready for zipping", "Klar for zipping", TransferStatusTypeEnum.ok),
    Zipping(21, "File is being zipped", "Zippes...", TransferStatusTypeEnum.running),
    Transfering(31, "File is being uploaded", "Overf?res...", TransferStatusTypeEnum.running),
    TransferredOk(32, "File has been transferred OK", "Overf?rt OK", TransferStatusTypeEnum.ok),
    TransferredError(33, "Filetransfer failed", "Overf?r med feil", TransferStatusTypeEnum.error),
    Transferred(40, "File has been moved to Transferred", "Overf?rt OK", TransferStatusTypeEnum.ok);

    int id;
    String longDescription;
    String description;
    TransferStatusTypeEnum statusType;

    TransferStatusEnum(int id, String longDescription, String description, TransferStatusTypeEnum statusType) {
        this.id = id;
        this.longDescription = longDescription;
        this.description = description;
        this.statusType = statusType;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public TransferStatusTypeEnum getStatusType() {
        return statusType;
    }
}