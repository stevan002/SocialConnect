package com.example.SocialConnect.service;

import com.example.SocialConnect.exception.BadRequestException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileServiceMinio{

    private final MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String bucketName;

    public String store(MultipartFile file, String serverFilename) {
        if (file.isEmpty()) {
            throw new BadRequestException("minio", "Failed to store empty file");
        }

        String[] originalFilenameTokens = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String extension = originalFilenameTokens[originalFilenameTokens.length - 1];

        try {
            System.out.println("Storing file: " + file.getOriginalFilename());
            System.out.println("Server Filename: " + serverFilename + "." + extension);

            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(serverFilename + "." + extension)
                    .headers(Collections.singletonMap("Content-Disposition",
                            "attachment; filename=\"" + file.getOriginalFilename() + "\""))
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .build();
            minioClient.putObject(args);
            System.out.println("File stored successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("minio" ,"Error while storing file in MinIO: " + e.getMessage());
        }

        return serverFilename + "." + extension;
    }


    public void delete(String serverFilename) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(serverFilename)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new BadRequestException("minio", "Error while deleting file in MinIO");
        }
    }

    public GetObjectResponse loadAsResource(String serverFilename) {
        try {
            GetPresignedObjectUrlArgs argsDownload = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(serverFilename)
                    .expiry(60 * 5)
                    .build();

            String downloadUrl = minioClient.getPresignedObjectUrl(argsDownload);
            System.out.println(downloadUrl);

            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(serverFilename)
                    .build();
            return minioClient.getObject(args);
        } catch (Exception e) {
            throw new BadRequestException("minio", "Document does not exist in MinIO");
        }
    }
}
