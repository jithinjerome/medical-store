package com.bank.AWS;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileUploadService {

    @Autowired
    private com.bank.AWS.S3Config s3Config;

    @Autowired
    private S3Client s3Client;



    public String uploadFile(String bucketName, String keyName, byte[] fileContent){
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType("image/png")
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileContent));
        return "https://"+bucketName+".s3."+s3Config.region+".amazon.com/"+keyName;
    }

}
