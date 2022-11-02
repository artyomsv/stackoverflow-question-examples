package com.stukans.refirmware.agent.marlin.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;

@TestConfiguration
public class TestLocalStackConfiguration {

    @Bean
    public AmazonS3 amazonS3(LocalStackTestContainer localStackTestContainer) {
        LocalStackContainer localStack = localStackTestContainer.getLocalStackContainer();
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    localStack.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
                    localStack.getRegion()
                )
            )
            .withCredentials(
                new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(localStack.getAccessKey(), localStack.getSecretKey())
                )
            )
            .build();
    }

}
