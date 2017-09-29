package no.fjordkraft.im.services;

import no.fjordkraft.im.model.BrandConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miles on 6/5/2017.
 */
@Service
public interface BrandService {
    BrandConfig getBrandConfigByName(String brand);
    List<String> getBrandsList();
}
