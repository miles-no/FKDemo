package no.fjordkraft.im.services.impl;

import no.fjordkraft.im.model.GridGroup;
import no.fjordkraft.im.repository.GridGroupRepository;
import no.fjordkraft.im.services.GridGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 1/3/18
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GridGroupServiceImpl implements GridGroupService {

    @Autowired
    GridGroupRepository gridGroupRepository;

    @Override
    public List<GridGroup> getGridGroupByGridConfigName(String gridName) {
        return gridGroupRepository.getGridGroupByGridConfigName(gridName);
    }

}
