package no.fjordkraft.im.model;

import javax.persistence.*;

/**
 * Created by miles on 6/12/2017.
 */
@Entity
@Table(name="IM_GRID_CONFIG")
public class GridConfig {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_GRID_CONFIG_SEQ")
    private Long id;

    @Column(name="GRID_NAME")
    private String gridName;

    @Column(name="GRID_LABEL")
    private String gridLabel;

    @Column(name="EMAIL")
    private String email;

    @Column(name="PHONE")
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGridLabel() {
        return gridLabel;
    }

    public void setGridLabel(String gridLabel) {
        this.gridLabel = gridLabel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
