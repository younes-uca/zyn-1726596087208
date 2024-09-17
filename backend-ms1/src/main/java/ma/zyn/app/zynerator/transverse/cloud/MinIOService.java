package ma.zyn.app.zynerator.transverse.cloud;

import io.minio.errors.MinioException;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface MinIOService {
    MinIOInfos uploadToMinio(MultipartFile file, String bucket);

    boolean bucketExists(String name);

    List<MinIOInfos> uploadMultipleToMinio(List<MultipartFile> files, String bucket);

    int saveBucket(String bucket);

    List<String> getAllDocumentsNames(String bucket) throws MinioException;

    byte[] LoadDocument(String bucket, String documentName) throws MinioException;

    int setObjectTags(String bucketName, String objectName, Map<String, String> tags) throws MinioException;

}
