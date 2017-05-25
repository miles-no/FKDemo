package no.fjordkraft.im.repository;



import no.fjordkraft.im.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, String> {

    public List<Config> findByNameLike(String name);
}
