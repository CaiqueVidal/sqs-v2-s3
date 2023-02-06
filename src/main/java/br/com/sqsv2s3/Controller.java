package br.com.sqsv2s3;

import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sqs")
public class Controller {

    private final SqsClient sqs;

    public Controller(SqsClient sqs) {
        this.sqs = sqs;
    }

    @PostMapping
    public void createQueue() {
        try {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName("MyQueue" + new Date().getTime())
                    .build();
            sqs.createQueue(createQueueRequest);
        } catch (SqsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }

    @GetMapping
    public String listQueues() {
        StringBuffer sBQueues = new StringBuffer();
        try {
//            ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix("queue").build();
//            ListQueuesResponse listQueuesResponse = sqs.listQueues(listQueuesRequest);
            ListQueuesResponse listQueuesResponse = sqs.listQueues();

            for (String url : listQueuesResponse.queueUrls()) {
                sBQueues.append(url + "\n");
            }
        } catch (SqsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }

        return sBQueues.toString();
    }

    @DeleteMapping("/{id}")
    public void deleteQueue(@PathVariable String id) {
        try {
            DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder()
                    .queueUrl("https://sqs.sa-east-1.amazonaws.com/693319179378/" + id)
                    .build();
            sqs.deleteQueue(deleteQueueRequest);
        } catch (SqsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/message")
    public void sendMessage(@RequestBody Sqs message) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(message.getQueue())
                    .messageBody(message.getMessage())
                    .delaySeconds(5)
                    .build();
            sqs.sendMessage(sendMessageRequest);
        } catch (SqsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }

    @PostMapping("/l-message")
    public void sendLargeMessage(@RequestBody Sqs message) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(message.getQueue())
                    .messageBody(longString(message.getMessage().charAt(0)))
                    .delaySeconds(5)
                    .build();
            sqs.sendMessage(sendMessageRequest);
        } catch (SqsException e) {
            System.out.println(e.awsErrorDetails().errorMessage());
        }
    }

    @GetMapping("/message/{id}")
    public String receiveMessage(@PathVariable String id) {
        String url = "https://sqs.sa-east-1.amazonaws.com/693319179378/" + id;

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(url)
                .maxNumberOfMessages(3)
                .build();

        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).messages();

        StringBuffer sBMessages = new StringBuffer();
        for (Message message : messages) {
            sBMessages.append(message.body());

            deleteMessage(url, message);
        }

        return sBMessages.toString();
    }

    private void deleteMessage(String url, Message message) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(url)
                .receiptHandle(message.receiptHandle())
                .build();

        sqs.deleteMessage(deleteMessageRequest);
    }

    private String longString(Character letter) {
        int stringLength = 300000;
        char[] chars = new char[stringLength];
        Arrays.fill(chars, letter);
        return new String(chars);
    }
}
