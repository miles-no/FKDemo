package no.fjordkraft.afi.server.emuxml.services;

import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuXmlHandleRequest;

public interface ParseEmuXmlService {

    /**
     * Parses a EMU XML file.
     * <p>
     * Will store attacment files and one IF318 file.
     *
     * @param request
     * @param emuFromPath location of the emu - path
     * @param toAttachmentPath   location to store the attachment file
     * @param to318Path   location to store 318 file
     */
    void parseFile(EmuXmlHandleRequest request, String emuFromPath, String toAttachmentPath, String to318Path);

    /**
     * Parses an EMU XML file.  From- and to-paths are fetched from config.
     *
     * @param request
     */
    void parseFile(EmuXmlHandleRequest request);

}
