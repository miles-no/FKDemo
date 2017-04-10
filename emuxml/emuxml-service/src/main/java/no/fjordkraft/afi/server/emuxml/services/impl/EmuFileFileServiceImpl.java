package no.fjordkraft.afi.server.emuxml.services.impl;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFile;
import no.fjordkraft.afi.server.emuxml.jpa.domain.EmuFilesRequest;
import no.fjordkraft.afi.server.emuxml.jpa.emu.BaseEmuParser;
import no.fjordkraft.afi.server.emuxml.services.EmuFileFileService;
import no.fjordkraft.afi.server.emuxml.services.EmuXmlConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("EmuFileFileService")
public class EmuFileFileServiceImpl extends BaseEmuParser implements EmuFileFileService {

    @Resource
    private EmuXmlConfig emuXmlConfig;

    @Override
    public List<EmuFile> getFilesFromSource(EmuFilesRequest request) {
        return getFilesInDirectory(request);
    }

    private List<EmuFile> getFilesInDirectory(EmuFilesRequest request) {
        Stopwatch sw = Stopwatch.createStarted();
        String directory = emuXmlConfig.getEmuXmlDirectory();
        List<EmuFile> files = getFilesInDirectory(directory, ".*\\.xml").stream()
                .filter(file -> !file.isDirectory())
                .map(file -> new EmuFile(fixFilename(file.getName()), file.lastModified(), file.length()))
                .collect(Collectors.toList());
        log.info(String.format("getFilesInDirectory: %s -> %d  (%s)",
                directory,
                files.size(),
                sw.stop().toString()
        ));
        return files;
    }

    private List<File> getFilesInDirectory(String directory, String fileFilter) {
        if (directory == null) {
            log.error("getFilesInDirectory: missing directory");
            return new ArrayList<>();
        }
        File input = new File(directory);
        if (!input.isDirectory()) {
            return new ArrayList<>();
        }

        FilteredFilenameFilter filter = new FilteredFilenameFilter(fileFilter == null ? ".*" : fileFilter);

        File[] files = input.listFiles(filter);
        if (files == null || files.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.asList(files);
    }

    private static class FilteredFilenameFilter implements FilenameFilter {
        private final Pattern pattern;

        public FilteredFilenameFilter(String fileFilter) {
            pattern = Pattern.compile(fileFilter);
        }

        @Override
        public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            Matcher matcher = pattern.matcher(name);
            return matcher.matches();
        }
    }

    private String fixFilename(String name) {
        return name;
    }

}
