package com.blueOcean.humanResourceSystem.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Paths;

@Component
public class S3UploaderAndComparison {
    private S3Client s3;


    // Constructor for reusability
    public S3UploaderAndComparison(@Value("${AWS_ACCESS_KEY}") String accessKey,
                                   @Value("${AWS_SECRET_KEY}") String secretKey,
                                   @Value("${aws.region}") String region) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public void uploadFile(String bucketName, String key, String filePath) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PutObjectResponse response = s3.putObject(putObjectRequest, RequestBody.fromFile(Paths.get(filePath)));
            System.out.println("File uploaded successfully: " + response.eTag());
        } catch (S3Exception e) {
            System.err.println("S3 Upload Error: " + e.awsErrorDetails().errorMessage());
        }
    }

    // Method to fetch an image from S3 and load it into memory
    public BufferedImage fetchImageFromS3(String bucketName, String objectKey) {
        try {
            // Get the object from S3 bucket
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            InputStream objectData = s3.getObject(getObjectRequest);
            return ImageIO.read(objectData);  // Load image into memory as BufferedImage
        } catch (Exception e) {
            System.err.println("Error fetching image from S3: " + e.getMessage());
            return null;
        }
    }
//    public static void main(String[] args) {
//        // Fetch the AWS access key and secret key from environment variables
//        String accessKey = System.getenv("AWS_ACCESS_KEY");
//        String secretKey = System.getenv("AWS_SECRET_KEY");
//
//        // Ensure the access and secret keys are available
//        if (accessKey == null || secretKey == null) {
//            System.err.println("AWS_ACCESS_KEY or AWS_SECRET_KEY is not set in the environment variables.");
//            return;
//        }
//        S3Uploader uploader = new S3Uploader(accessKey, secretKey, "ca-central-1");
//        uploader.uploadFile("elasticbeanstalk-ca-central-1-471112778577", "user-profile/image.jpg", "C:\\Users\\10648\\Desktop\\co-op permit apply\\digital photo.jpg");
//    }
}
