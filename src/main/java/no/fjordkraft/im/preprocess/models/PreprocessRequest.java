package no.fjordkraft.im.preprocess.models;

/**
 * Created by bhavi on 5/8/2017.
 */
public class PreprocessRequest<T,K> {

    private T statement;

    private K entity;

    String pathToAttachmentPDF;

    public T getStatement() {
        return statement;
    }

    public void setStatement(T statement) {
        this.statement = statement;
    }

    public K getEntity() {
        return entity;
    }

    public void setEntity(K entity) {
        this.entity = entity;
    }

    public String getPathToAttachmentPDF() {
        return pathToAttachmentPDF;
    }

    public void setPathToAttachmentPDF(String pathToAttachmentPDF) {
        this.pathToAttachmentPDF = pathToAttachmentPDF;
    }
}
