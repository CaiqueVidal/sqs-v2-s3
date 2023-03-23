package br.com.sqsv2s3;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class Config {

    private String s3Name = "my-bucket-async";

    @Bean
    public SqsClient sqsClientExtend() {
        return new AmazonSQSExtendedClient(sqsClient(), extendedClientConfiguration());
    }

    private SqsClient sqsClient() {
        ClientOverrideConfiguration.Builder overrideConfig =
                ClientOverrideConfiguration.builder();

        RetryPolicy.Builder retryPolicy = RetryPolicy.builder();

        return SqsClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
//                .endpointOverride(URI.create("https://sqs.sa-east-1.amazonaws.com/693319179378"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .overrideConfiguration(overrideConfig.retryPolicy(retryPolicy.build()).build())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        ClientOverrideConfiguration.Builder overrideConfig =
                ClientOverrideConfiguration.builder();

        RetryPolicy.Builder retryPolicy = RetryPolicy.builder();

        return S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
//                .endpointOverride(URI.create("https://s3.sa-east-1.amazonaws.com"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .overrideConfiguration(overrideConfig.retryPolicy(retryPolicy.build()).build())
                .build();
    }

    private ExtendedClientConfiguration extendedClientConfiguration() {
        System.out.println(s3Name);
        return new ExtendedClientConfiguration().withPayloadSupportEnabled(s3Client(), s3Name);
    }
}