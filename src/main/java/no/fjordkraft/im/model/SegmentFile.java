package no.fjordkraft.im.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by miles on 6/21/2017.
 */
@Table(name="SEGMENTFILE", schema="eacprod")
@Entity
public class SegmentFile {
    @Column(name="ID")
    @Id
    private Long id;

    @Column(name="FILENAME")
    private String fileName;

    @Column(name="FILETYPE")
    private String fileType;

    @Column(name="FILECONTENT")
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String fileContent;

    @Column(name="BRAND")
    private String brand;

    @Column(name="CUSTOMERTYPE")
    private String customerType;

    @Column(name="STROMAVTALE")
    private String stromavtale;

    @Column(name="NETOWNERID")
    private Long netOwerID;

    @Column(name="JEVNSTROM")
    private Integer jevnstrom;

    @Column(name="AVTALEGIRO")
    private Integer avtalegiro;

    @Column(name="FORSTEFAKTURA")
    private Integer forsteFaktura;

    @Column(name="OPPHOR")
    private Integer opphor;

    @Column(name="UPLOADED")
    private Timestamp uploaded;

    @Column(name="CHANGED")
    private Timestamp changed;

    @Column(name="CHANGEDBY")
    private String changedBy;

    @Column(name="ORIGFILENAME")
    private String originalFileName;

    @Column(name="TARIFF")
    private String tariff;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getStromavtale() {
        return stromavtale;
    }

    public void setStromavtale(String stromavtale) {
        this.stromavtale = stromavtale;
    }

    public Long getNetOwerID() {
        return netOwerID;
    }

    public void setNetOwerID(Long netOwerID) {
        this.netOwerID = netOwerID;
    }

    public Integer getJevnstrom() {
        return jevnstrom;
    }

    public void setJevnstrom(Integer jevnstrom) {
        this.jevnstrom = jevnstrom;
    }

    public Integer getAvtalegiro() {
        return avtalegiro;
    }

    public void setAvtalegiro(Integer avtalegiro) {
        this.avtalegiro = avtalegiro;
    }

    public Integer getForsteFaktura() {
        return forsteFaktura;
    }

    public void setForsteFaktura(Integer forsteFaktura) {
        this.forsteFaktura = forsteFaktura;
    }

    public Integer getOpphor() {
        return opphor;
    }

    public void setOpphor(Integer opphor) {
        this.opphor = opphor;
    }

    public Timestamp getUploaded() {
        return uploaded;
    }

    public void setUploaded(Timestamp uploaded) {
        this.uploaded = uploaded;
    }

    public Timestamp getChanged() {
        return changed;
    }

    public void setChanged(Timestamp changed) {
        this.changed = changed;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }
}
