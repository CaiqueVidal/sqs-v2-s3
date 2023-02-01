package br.com.sqsv2s3;

import lombok.Data;

@Data
public class Sqs {
    private String message;
    private String queue;
}
