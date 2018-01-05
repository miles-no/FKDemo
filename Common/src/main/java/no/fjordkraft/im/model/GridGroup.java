package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="IM_GRID_GROUP")
public class GridGroup {
    @Column(name="GROUP_ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="IM_GRID_GROUP_SEQ")
    private Long id;

    @Column(name="GRID_GROUP_NAME")
    private String gridGroupName;

    @JsonIgnore
    @OneToMany(mappedBy = "gridGroup",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GroupGridLine> groupGridLines;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="GRID_CONFIG_ID")
    private GridConfig gridConfig;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGridGroupName() {
        return gridGroupName;
    }

    public void setGridGroupName(String gridGroupName) {
        this.gridGroupName = gridGroupName;
    }

    public GridConfig getGridConfig() {
        return gridConfig;
    }

    public void setGridConfig(GridConfig gridConfig) {
        this.gridConfig = gridConfig;
    }


    public List<GroupGridLine> getGroupGridLines() {
        return groupGridLines;
    }

    public void setGroupGridLines(List<GroupGridLine> groupGridLines) {
        this.groupGridLines = groupGridLines;
    }
}
