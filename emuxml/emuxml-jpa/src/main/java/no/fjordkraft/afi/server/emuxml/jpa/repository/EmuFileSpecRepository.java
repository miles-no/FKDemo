package no.fjordkraft.afi.server.emuxml.jpa.repository;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EmuFileSpecRepository {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<EmuFile> findByEmuFilesRequest(EmuFilesRequest request) {
        TypedQuery<EmuFile> q = entityManager.createQuery("" +
                        "SELECT ef " +
                        "FROM EmuFile ef " +
                        "WHERE " +
                        "ef.productionDate >= :startTime " +
                        "AND ef.productionDate <= :endTime " +
                        (request.getFilename() != null ? "AND ef.filename = :filename " : "") +
                        "",
                EmuFile.class)
                .setFirstResult(request.getPageRequest().getOffset())
                .setMaxResults(request.getPageRequest().getPageSize())
                .setParameter("startTime", request.getFromDate())
                .setParameter("endTime", request.getToDate());

        if (request.getFilename() != null) {
            q = q.setParameter("filename", request.getFilename());
        }
        List<EmuFile> result = q.getResultList();
        return result;
    }

}
