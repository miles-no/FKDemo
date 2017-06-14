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

    @Column(name="BRAND")
    private String brand;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
