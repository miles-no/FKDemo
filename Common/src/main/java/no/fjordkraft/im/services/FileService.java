package no.fjordkraft.im.services;

import java.util.List;

public interface FileService {

	StringBuilder readTextfileFromFilesystem(String path);

	StringBuilder readTextfileFromFilesystem(String path, String charset);

	boolean checkFilesInDirectory(String directory, String fileFilter);

	List<String> getFilesInDirectories(String directory);

	List<String> getFilesInDirectories(String directory, String fileFilter);

    List<String> getFilesInDirectory(String directory, String fileFilter);

    Boolean directoryExists(String dirToCheck);
}