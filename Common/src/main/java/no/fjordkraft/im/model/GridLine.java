package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="IM_GRID_LINE")
public class GridLine {

    @Column(name="GRID_LINE_ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_GRID_LINE_SEQ")
    private Long id;

    @Column(name="GRID_LINE_NME")
    private String gridLineName;

    @JsonIgnore
    @OneToMany(mappedBy = "gridLine", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<GroupGridLine> groupGridLines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGridLineName() {
        return gridLineName;
    }

    public void setGridLineName(String gridLineName) {
        this.gridLineName = gridLineName;
    }
}
