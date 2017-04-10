package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;

import java.util.List;

public interface EmuFileFetchService {

    /**
     * Fetches EmuFile.
     *
     * @param filename
     */
    EmuFile fetch(String filename);

    /**
     * Fetch EMU-files by request.
     *
     * @param request
     * @return
     */
    List<EmuFile> filterByRequest(EmuFilesRequest request);

}
