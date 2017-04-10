package no.fjordkraft.afi.server.emuxml.services;

public interface EmuXmlConfig {

    String EMU_XML_SOURCE_DIRECTORY = "emu.xml.source.directory";
    String EMU_XML_SCAN_DIRECTORY = "emu.xml.scan.directory";
    String EMU_XML_SCANNED_DIRECTORY = "emu.xml.scanned.directory";
    String EMU_XML_318_DESTINATION_DIRECTORY = "emu.xml.if318.dest.directory";
    String EMU_XML_ATTACH_DESTINATION_DIRECTORY = "emu.xml.attach.dest.directory";

    /**
     * Get EMU XML directory.  Source for EMY XML files.
     *
     * @return
     */
    String getEmuXmlDirectory();

    /**
     * Get Scan - directory.  EMU XML files are scanned from this directory.
     *
     * @return
     */
    String getScanDirectory();

    /**
     * Get Scanned - directory.  EMU XML files are moved to this directory
     * after they have been scanned.
     *
     * @return
     */
    String getScannedDirectory();

    /**
     * Get IF318 destination directory.  IF318 files are written to this
     * directory.
     *
     * @return
     */
    String getIF318DestinationDirectory();

    /**
     * Get the attachment destination directory.  Brand-code are replaced
     * with correct brand, e.g.
     * /mnt/fknt12/Export/Statements/{brand}/Attachments
     * to /mnt/fknt12/Export/Statements/FKAS/Attachments
     *
     * @return
     */
    String getAttachDestinationDirectory();
}
