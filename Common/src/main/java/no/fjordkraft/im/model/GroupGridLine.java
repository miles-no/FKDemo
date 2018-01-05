package no.fjordkraft.im.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Table(name="IM_GROUP_GRID_LINE")
@Entity
public class GroupGridLine {

    @Column(name="ID")
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator = "SEQ")
    @SequenceGenerator(name="SEQ", sequenceName="im_group_grid_line_seq")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="GRID_LINE_ID")
    private GridLine gridLine;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="GRID_GROUP_ID")
    private GridGroup gridGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GridLine getGridLine() {
        return gridLine;
    }

    public void setGridLine(GridLine gridLine) {
        this.gridLine = gridLine;
    }

    public GridGroup getGridGroup() {
        return gridGroup;
    }

    public void setGridGroup(GridGroup gridGroup) {
        this.gridGroup = gridGroup;
    }
}
