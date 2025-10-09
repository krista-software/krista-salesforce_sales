package app.krista.extensions.crm.sales.salesforce_sales.impl.stores;

import app.krista.ksdk.files.FileHandle;
import app.krista.ksdk.files.FileRepository;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class KristaMediaClient {

    private static final String zipDir = System.getProperty("java.io.tmpdir");
    @Inject
    private FileRepository fileRepository;

    private final List<String> unSupportedFileFormats = Arrays.asList("html", "php5", "pht", "phtml", "shtml", "asa", "cer", "asax", "swf", "xap", "jsp", "exe", "js");

    /**
     * Uploads a file to Krista's media server. it will take java.io.File object as input and returns krista's file object
     *
     * @param file The file to be uploaded.
     * @return The Krista file object.
     * @throws IOException If an I/O error occurs.
     */
    public app.krista.model.base.File toKristaFile(File file) throws IOException {
        // If supported, upload as-is preserving the original name
        if (!isUnsupportedFileFormat(file.getName())) {
            try (final FileHandle fileHandle = fileRepository.createNewFileByName(file.getName());
                 final FileInputStream fis = new FileInputStream(file)) {
                fileHandle.setContent(fis);
                return fileHandle.getFile();
            }
        }

        // For unsupported formats, compress to a uniquely-named temp zip but preserve the external name
        String baseName = file.getName();
        int dot = baseName.lastIndexOf('.')
;        if (dot > 0) {
            baseName = baseName.substring(0, dot);
        }
        String originalZipName = baseName + ".zip";

        Path tmpZip = Files.createTempFile("krista-upload-", ".zip");
        try {
            compressFile(tmpZip.toString(), file.getAbsolutePath());
            try (final FileHandle fileHandle = fileRepository.createNewFileByName(originalZipName);
                 final FileInputStream fis = new FileInputStream(tmpZip.toFile())) {
                fileHandle.setContent(fis);
                return fileHandle.getFile();
            }
        } finally {
            try { Files.deleteIfExists(tmpZip); } catch (IOException ignore) {}
        }
    }

    /**
     * Downloads a file from Krista's media server. it will take krista's file object as input and returns java.io.File object
     *
     * @param file The Krista file object to be downloaded.
     * @return The downloaded Java file object.
     * @throws IOException If an I/O error occurs.
     */
    public File toJavaFile(app.krista.model.base.File file) throws IOException {
        try (FileHandle fileHandle = fileRepository.getFile(file);
             InputStream content = fileHandle.getContent()) {
            final File input = new File(file.getFileName());
            return convertInputStreamToFile(content, input);
        }
    }

    /**
     * Checks if the file format is unsupported.
     *
     * @param fileName The name of the file.
     * @return True if the file format is unsupported, otherwise false.
     */
    private boolean isUnsupportedFileFormat(String fileName) {
        String fileExtension = getFileExtension(fileName);
        return unSupportedFileFormats.contains(fileExtension);
    }

    /**
     * Converts an input stream to a file.
     *
     * @param inputStream The input stream to be converted.
     * @param input       The file to write the input stream content to.
     * @return The file with the content of the input stream.
     * @throws IOException If an I/O error occurs.
     */
    private File convertInputStreamToFile(InputStream inputStream, File input) throws IOException {
        try (InputStream in = inputStream; OutputStream out = new FileOutputStream(input)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return input;
    }

    /**
     * Retrieves the file extension from the file name.
     *
     * @param fileName The name of the file.
     * @return The file extension.
     * @throws IllegalArgumentException If the file format is unsupported.
     */
    private String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring((fileName.lastIndexOf(".") + 1));
        }
        throw new IllegalArgumentException("Unsupported file format");
    }

    /**
     * Compresses a file into a zip archive.
     *
     * @param zipFilePath  The path where the zip file will be created.
     * @param dirPathToZip The path of the directory to be zipped.
     * @throws IOException If an I/O error occurs.
     */
    public static void compressFile(String zipFilePath, String sourceFilePath) throws IOException {
        File source = new File(sourceFilePath);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
             FileInputStream fis = new FileInputStream(source)) {
            zipOut.putNextEntry(new ZipEntry(source.getName()));
            fis.transferTo(zipOut);
            zipOut.closeEntry();
        }
    }
}