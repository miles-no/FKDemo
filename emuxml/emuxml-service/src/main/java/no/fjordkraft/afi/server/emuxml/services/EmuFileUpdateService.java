package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;

public interface EmuFileUpdateService {

    /**
     * Create EmuFile.
     *
     * @param request
     */
    EmuFile create(EmuXmlHandleRequest request);

    /**
     * Fetches EmuFile.
     *
     * @param emuFile
     */
    EmuFile update(EmuFile emuFile);

}
