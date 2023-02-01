package br.com.sqsv2s3;

import com.amazon.sqs.javamessaging.AmazonSQSExtendedClient;
import com.amazon.sqs.javamessaging.ExtendedClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
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
        return SqsClient.builder().region(Region.SA_EAST_1).credentialsProvider(ProfileCredentialsProvider.create()).build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.SA_EAST_1).credentialsProvider(ProfileCredentialsProvider.create()).build();
    }

    private ExtendedClientConfiguration extendedClientConfiguration() {
        return new ExtendedClientConfiguration().withPayloadSupportEnabled(s3Client(), s3Name);
    }
}