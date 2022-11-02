package com.stukans.refirmware.agent.marlin.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3ClientConfiguration {

    @Bean
    public AmazonS3 amazonS3(@Value("${space.endpoint}") String endpoint,
                             @Value("${space.region}") String region,
                             @Value("${space.key}") String key,
                             @Value("${space.secret}") String secret) {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secret)))
            .build();
    }

}
