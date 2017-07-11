package no.fjordkraft.im.repository;

import no.fjordkraft.im.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by miles on 7/10/2017.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
