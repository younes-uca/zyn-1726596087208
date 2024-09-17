package ma.zyn.app.zynerator.transverse.cloud;


import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.VersioningConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MinIOServiceImpl implements MinIOService {
   @Autowired
    private MinioClient minioClient;
    @Override
    public boolean bucketExists(String name) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean objectExists(String bucketName, String objectName) throws MinioException {
        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true; // Object exists
        } catch (MinioException e) {
            // Check if the exception is due to object not found
            if (e instanceof ErrorResponseException && ((ErrorResponseException) e).errorResponse().code().equals("NoSuchKey")) {
                return false; // Object not found
            }
            // If it's not NoSuchKey, throw the MinioException
            throw e;
        } catch (Exception e) {
            throw new MinioException("An error occurred while checking if the object " + objectName + " exists: " + e.getMessage());
        }
    }

    @Override
    public MinIOInfos uploadToMinio(MultipartFile file, String bucket) {
        if (!bucketExists(bucket))
            return null;
        try {
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            long size = file.getSize();
            InputStream inputStream = file.getInputStream();
            // Read the InputStream into a byte array
            byte[] bytes = file.getBytes();
            // Create a new InputStream from the byte array
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            String objectName = constructObjectName(originalFilename);
            // Upload the object using the new InputStream
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(byteArrayInputStream, bytes.length, -1)
                            .contentType(contentType)
                            .build()
            );
            // Close the ByteArrayInputStream after use
            byteArrayInputStream.close();
            String etag = minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucket).object(objectName).build()
            ).etag();
            int resultStatus = (etag != null) ? 1 : 0;
            MinIOInfos minIOInfos = new MinIOInfos(bucket, originalFilename, size, bytes, etag, resultStatus);
            return minIOInfos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<MinIOInfos> uploadMultipleToMinio(List<MultipartFile> files, String bucket) {
        List<MinIOInfos> minIOInfosList = new ArrayList<>();
        if (!bucketExists(bucket))
            return null;
        for (MultipartFile file : files) {
            try {
                String originalFilename = file.getOriginalFilename();
                String contentType = file.getContentType();
                long size = file.getSize();
                InputStream inputStream = file.getInputStream();
                byte[] bytes = file.getBytes();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                String objectName = constructObjectName(originalFilename);
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucket)
                                .object(objectName)
                                .stream(byteArrayInputStream, bytes.length, -1)
                                .contentType(contentType)
                                .build()
                );
                byteArrayInputStream.close();
                String etag = minioClient.statObject(
                        StatObjectArgs.builder().bucket(bucket).object(objectName).build()
                ).etag();

                int resultStatus = (etag != null) ? 1 : 0;
                MinIOInfos minIOInfos = new MinIOInfos(bucket, originalFilename, size, bytes, etag, resultStatus);
                minIOInfosList.add(minIOInfos);

            } catch (Exception e) {
                e.printStackTrace();
                // You might want to handle this differently, such as logging the error and continuing with the next file
            }
        }
        return minIOInfosList;
    }


    private String constructObjectName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    @Override
    public int saveBucket(String bucket) {
        if (bucketExists(bucket))
            return 0;
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            VersioningConfiguration config = new VersioningConfiguration(VersioningConfiguration.Status.ENABLED, false);
            minioClient.setBucketVersioning(SetBucketVersioningArgs.builder().bucket(bucket).config(config).build());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<String> getAllDocumentsNames(String bucket) throws MinioException {
        if (Boolean.FALSE.equals(bucketExists(bucket))) {
            throw new BucketNotFoundException("the bucket " + bucket + " does not exist");
        }
        List<String> documents = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).recursive(true).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                documents.add(item.objectName());
            }
        } catch (Exception e) {
            throw new MinioException("Error while fetching files form the bucket " + bucket + ", error : " + e.getMessage());
        }
        return documents;
    }

    @Override
    public byte[] LoadDocument(String bucket, String documentName) throws MinioException {
        if (!bucketExists(bucket)) {
            throw new BucketNotFoundException("The bucket " + bucket + " does not exist");
        }
        try {
            // Get the document object from MinIO
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(documentName)
                            .build()
            );
            // Get the input stream containing the document data
            InputStream documentStream = response;
            // Create a byte array output stream to hold the document data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Buffer for reading data
            byte[] buffer = new byte[8192];
            int bytesRead;
            // Write the document data to the byte array output stream
            while ((bytesRead = documentStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            // Close the input stream for the document
            documentStream.close();

            // Return the document data as a byte array
            return baos.toByteArray();
        } catch (Exception e) {
            throw new MinioException("Error while downloading the document " + documentName + " from the bucket " + bucket + ", error : " + e.getMessage());
        }
    }

    @Override
    public int setObjectTags(String bucketName, String objectName, Map<String, String> tags) throws MinioException {
        try {
            if (!bucketExists(bucketName)) {
                throw new BucketNotFoundException("The bucket " + bucketName + " does not exist");
            }
            if (!objectExists(bucketName, objectName)) {
                throw new BucketNotFoundException("The object " + bucketName + " does not exist in the bucket " + bucketName);
            }
            minioClient.setObjectTags(
                    SetObjectTagsArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .tags(tags)
                            .build()
            );
            return 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }


}
