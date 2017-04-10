package no.fjordkraft.afi.server.emuxml.jpa.repository;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmuFileRepository extends JpaRepository<EmuFile, Long> {
    EmuFile findByFilename(String filename);
}
