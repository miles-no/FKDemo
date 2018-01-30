package no.fjordkraft.im.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/23/18
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "IM_BLANKET_NUMBER")
public class BlanketNumber {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_BLANKET_NUMBER_SEQ")
    private Long id;

    @Column(name="BLANKET_NUMBER")
    private String blanketNumber;

    @Column(name="DATE_OF_ACTIVATION")
    private Date dateOfActivation;

/*
    @Column(name="DATE_OF_EXPIRATION")
    private Date dateOfExpiration;*/

    @Column(name="MODE_OF_GENERATION")
    private String modeOfGeneration;

    @Column(name="STATUS")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlanketNumber() {
        return blanketNumber;
    }

    public void setBlanketNumber(String blanketNumber) {
        this.blanketNumber = blanketNumber;
    }

    public Date getDateOfActivation() {
        return dateOfActivation;
    }

    public void setDateOfActivation(Date dateOfActivation) {
        this.dateOfActivation = dateOfActivation;
    }

    public String getModeOfGeneration() {
        return modeOfGeneration;
    }

    public void setModeOfGeneration(String modeOfGeneration) {
        this.modeOfGeneration = modeOfGeneration;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
