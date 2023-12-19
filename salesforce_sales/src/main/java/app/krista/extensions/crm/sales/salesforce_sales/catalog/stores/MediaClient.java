package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.authorization.MustAuthorizeException;
import app.krista.extensions.crm.sales.salesforce_sales.util.Constants;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM_TYPE;

public final class MediaClient {

    public static final String TEMP_DIR = "/tmp/krista/extensions/";
    private static MediaClient mediaClient;
    private final WebTarget webTarget;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private MediaClient() {
        webTarget = ClientBuilder.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .register(MultiPartFeature.class).build()
                .target(Constants.MEDIA_URI);
        new File(TEMP_DIR).mkdirs();
    }

    public static MediaClient getInstance() {
        if (mediaClient == null) {
            mediaClient = new MediaClient();
        }
        return mediaClient;
    }

    public static String getNextMediaId(String fileExtension) {
        return "doc_" + fileExtension + "_" + UUID.randomUUID() + "." + fileExtension;
    }

    public void upload(String url, File file) {
        validateEmptyInput(url, "Failed to upload file. URL not found.");
        validateEmptyInput(file, "Failed to upload file. File not found.");
        WebTarget resourceTarget = webTarget.path(url);
        resourceTarget = resourceTarget.queryParam("len", file.length());
        handleResponse(getUploadResponse(file, resourceTarget));
    }

    public File download(String url, String fileName) throws IOException {
        validateEmptyInput(url, "Failed to download file. URL not found.");
        validateEmptyInput(fileName, "Failed to download file. File name not found.");
        Response response = webTarget.path(url).request(MediaType.APPLICATION_OCTET_STREAM).get();
        return handleResponse(response) ? getJavaFileFromResponse(response, fileName) : null;
    }

    public String createMediaUrl(String mediaId) {
        return "/media/public/" + mediaId;
    }

    private boolean handleResponse(Response response) {
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL)
            return true;
        else if (response.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode())
            throw new MustAuthorizeException(response.getStatusInfo().getReasonPhrase());
        else throw new ClientErrorException(response);
    }

    public String getMediaID(String fileExtension) {
        if (fileExtension == null || fileExtension.isBlank()) {
            fileExtension = "txt";
        }
        return "doc_binary_" + UUID.randomUUID() + "." + fileExtension;
    }

    private Response getUploadResponse(File file, WebTarget resourceTarget) {
        return resourceTarget.request().post(Entity.entity(file, APPLICATION_OCTET_STREAM_TYPE));
    }

    private File getJavaFileFromResponse(Response response, String fileName) throws IOException {
        InputStream is = response.readEntity(InputStream.class);
        File file = new File(TEMP_DIR + fileName);
        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return file;
    }

    private void validateEmptyInput(Object input, String errorMessage) {
        if (input == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (input instanceof String && ((String) input).isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
