package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.Export;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 5/12/2017.
 */
@Repository
public interface ExportRepository extends JpaRepository<Export, Long> {
}
