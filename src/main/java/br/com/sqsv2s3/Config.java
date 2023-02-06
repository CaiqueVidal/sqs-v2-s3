package br.com.sqsv2s3;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class Config {

    @Value("${s3.name}")
    private String s3Name;

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
                .overrideConfiguration(overrideConfig.retryPolicy(retryPolicy.build()).build())
                .build();
    }

    private ExtendedClientConfiguration extendedClientConfiguration() {
        return new ExtendedClientConfiguration().withPayloadSupportEnabled(s3Client(), s3Name);
    }
}