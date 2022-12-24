package com.ShopIT.Service;

import com.ShopIT.Config.AppConstants;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServices {
    @Value("${application.bucket.name}")
    private String bucketName;
    private final AmazonS3 s3Client;
//UploadToS3
    public String uploadFile(MultipartFile file){
        File fileObj = convertMultiPartFileToFile(file);
        String  filename = file.getOriginalFilename();
        String randomID = UUID.randomUUID().toString();
        assert filename != null;
        String newFileName =  randomID.concat(filename.substring(filename.lastIndexOf(".")));
        String filepath = AppConstants.path + newFileName;
        s3Client.putObject(new PutObjectRequest(bucketName, newFileName, fileObj));
        fileObj.delete();
        return filepath;
    }
//Serve File from S3
    public byte[] downloadFile(String fileName){
        S3Object s3Object= s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try{
            byte[] content  = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
//Delete the file from the server
    public String deleteFile(String filename){
        s3Client.deleteObject(bucketName, filename);
        return filename+" has been successfully deleted";
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream((convertedFile))){
            fos.write(file.getBytes());
        }
        catch (IOException e){
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
