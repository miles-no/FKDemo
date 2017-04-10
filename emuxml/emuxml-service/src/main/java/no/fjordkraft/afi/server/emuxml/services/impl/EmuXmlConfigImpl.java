package no.fjordkraft.afi.server.emuxml.services.impl;

import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.basis.services.ConfigService;
import no.fjordkraft.afi.server.emuxml.services.EmuXmlConfig;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("EmuXmlConfig")
public class EmuXmlConfigImpl implements EmuXmlConfig {

    @Autowired
    private ConfigService configService;

    @Override
    public String getEmuXmlDirectory() {
        return replaceAfiDir(configService.getDirectory(EMU_XML_SOURCE_DIRECTORY));
    }

    @Override
    public String getScanDirectory() {
        return replaceAfiDir(configService.getDirectory(EMU_XML_SCAN_DIRECTORY));
    }

    @Override
    public String getScannedDirectory() {
        return replaceAfiDir(configService.getDirectory(EMU_XML_SCANNED_DIRECTORY));
    }

    @Override
    public String getIF318DestinationDirectory() {
        return replaceDate(replaceAfiDir(configService.getDirectory(EMU_XML_318_DESTINATION_DIRECTORY)));
    }

    @Override
    public String getAttachDestinationDirectory() {
        return replaceDate(replaceAfiDir(configService.getDirectory(EMU_XML_ATTACH_DESTINATION_DIRECTORY)));
    }

    private String replaceAfiDir(String directory) {
        return directory != null ? directory.replace("<afi_homedir>", configService.getString(ConfigService.AFI_HOME_DIR)) : null;
    }

    private String replaceDate(String directory) {
        return directory != null ? directory.replace("<current_date>", ISODateTimeFormat.date().print(new DateTime())) : null;
    }
}
