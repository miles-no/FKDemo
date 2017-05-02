package no.fjordkraft.im.repository;


import no.fjordkraft.im.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, String> {

    public List<Config> findByKeyLike(String key);
}
