package no.fjordkraft.im.services.impl;

import com.google.common.collect.Lists;
import no.fjordkraft.im.exceptions.FileNotFoundException;
import no.fjordkraft.im.services.FileService;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(value = "fileService")
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private StringBuilder readBuffer(BufferedReader in) {
        if (in == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        try {
            String line;
            while ((line = in.readLine()) != null) { // Read line, check for
                // end-of-file
                buf.append(line);
                buf.append("\n");
            }
        } catch (IOException e) {
            logger.error("IOException reading file", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.error("Error closing resource stream", e);
            }
        }
        return buf;
    }

    @Override
    public StringBuilder readTextfileFromFilesystem(String path) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
        } catch (IOException e) {
            throw new FileNotFoundException("Unable to find file: " + path, e);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        return readBuffer(in);
    }

    @Override
    public StringBuilder readTextfileFromFilesystem(String path, String charset) {

        InputStream is = null;
        try {
            is = new FileInputStream(new File(path));
        } catch (IOException e) {
            throw new FileNotFoundException("Unable to find file: " + path, e);
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(is, charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("error reading file", e);
        }
        return readBuffer(in);
    }

    @Override
    public boolean checkFilesInDirectory(String directory, String fileFilter) {
        List<String> files = getFilesInDirectory(directory, fileFilter);
        return (files.size() > 0);
    }


    @Override
    public List<String> getFilesInDirectories(String directory) {
        List<String> filelist = new ArrayList<>();
        getFilesRecursive(directory, ".*", filelist);
        return filelist;
    }

    @Override
    public List<String> getFilesInDirectory(String directory, String fileFilter) {
        if (fileFilter == null) {
            fileFilter = ".*";
        }
        List<String> filelist = Lists.newArrayList();

        File input = new File(directory);
        if (input == null || !input.isDirectory()) {
            return Lists.newArrayList();
        }

        FilenameFilterImpl filter = new FilenameFilterImpl(fileFilter);

        File[] files = input.listFiles(filter);
        if (files == null || files.length == 0) {
            return Lists.newArrayList();
        }
        int size = files.length;
        for (int i = 0; i < size; i++) {
            if (files[i].isFile()) {
                String path = files[i].getPath();
                path = path.replace('\\', '/');
                if (path.toLowerCase().startsWith(directory.toLowerCase())) {
                    path = path.substring(directory.length());
                }
                filelist.add(path);
            }
        }

        return filelist;
    }

    @Override
    public Boolean directoryExists(String dirToCheck) {
        if(StringUtils.isBlank(dirToCheck)){
            return Boolean.FALSE;
        }
        File dir = new File(dirToCheck);
        return dir.exists() && dir.isDirectory();
    }

    @Override
    public List<String> getFilesInDirectories(String directory,
                                              String fileFilter) {
        List<String> filelist = new ArrayList<String>();
        getFilesRecursive(directory, fileFilter, filelist);
        return filelist;
    }


    private static void getFilesRecursive(String directory, String fileFilter,
                                          List<String> filelist) {
        try {
            File input = new File(directory);
            if (input == null || !input.isDirectory()) {
                return;
            }

            fileFilter = "(?i)" + fileFilter;
            RegexFileFilter regexFileFilter = new RegexFileFilter(fileFilter);

            File[] files = input.listFiles((FilenameFilter) regexFileFilter);
            if (files == null || files.length == 0) {
                return;
            }
            int size = files.length;
            for (int i = 0; i < size; i++) {
                if (files[i].isFile()) {
                    String path = files[i].getPath();
                    path = path.replace('\\', '/');
                    if (path.toLowerCase().startsWith(directory.toLowerCase())) {
                        path = path.substring(directory.length());
                    }
                    filelist.add(path);
                } else if (files[i].isDirectory()) {
                    String dir = files[i].getPath();
                    getFilesRecursive(dir, fileFilter, filelist);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "error scanning directory: " + directory, e);
        }
    }

    private static class FilenameFilterImpl implements FilenameFilter {

        private final Pattern pattern;

        public FilenameFilterImpl(String fileFilter) {
            pattern = Pattern.compile(fileFilter);
        }

        @Override
        public boolean accept(File dir, String name) {
            name = name.toLowerCase();
            Matcher matcher = pattern.matcher(name);
            return matcher.matches();
        }
    }

}
