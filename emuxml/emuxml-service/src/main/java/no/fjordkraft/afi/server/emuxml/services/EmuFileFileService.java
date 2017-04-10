package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;

import java.util.List;

public interface EmuFileFileService {

    /**
     * Fetches emu files, with filename and size, from ISCU source directory.
     *
     * @return
     */
    List<EmuFile> getFilesFromSource(EmuFilesRequest request);

}
