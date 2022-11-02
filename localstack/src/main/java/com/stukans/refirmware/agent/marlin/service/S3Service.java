package com.stukans.refirmware.agent.marlin.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.services.s3.model.HeadBucketResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;
    private final String space;

    public S3Service(AmazonS3 amazonS3, @Value("${space.space}") String space) {
        this.amazonS3 = amazonS3;
        this.space = space;
    }

    public Mono<byte[]> download(String key) {
        GetObjectRequest request = new GetObjectRequest(space, key);
        S3Object object = amazonS3.getObject(request);
        try (InputStream is = object.getObjectContent();) {
            byte[] bytes = IOUtils.toByteArray(is);
            return Mono.just(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PutObjectResult upload(String key, String fileName, String contentType, byte[] data) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentDisposition(String.format("attachment; filename=%s;", fileName));
        metadata.setContentLength(data.length);

        PutObjectRequest request = new PutObjectRequest(space, key, new ByteArrayInputStream(data), metadata);
        try {
            return amazonS3.putObject(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void isBucketRegistered() {
        HeadBucketRequest request = new HeadBucketRequest(space);
        HeadBucketResult result = amazonS3.headBucket(request);
        System.out.println(result.getBucketRegion());
    }

    public void createBucket() {
        CreateBucketRequest request = new CreateBucketRequest(space);
        Bucket bucket = amazonS3.createBucket(request);
        System.out.println(bucket.getName());
    }

    public void delete(String key) {
        amazonS3.deleteObject(new DeleteObjectRequest(space, key));
    }

}
