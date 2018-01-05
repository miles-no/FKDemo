package no.fjordkraft.im.services;

import no.fjordkraft.im.model.GridGroup;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridGroupService {

    List<GridGroup> getGridGroupByGridConfigName(String gridName);
}
