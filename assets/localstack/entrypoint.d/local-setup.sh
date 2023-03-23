#!/bin/bash
echo "########### Creating SQS ###########"
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name local_queue
echo "########### Creating S3 ###########"
aws --endpoint-url=http://localhost:4566 s3 mb s3://my-bucket
echo "### versão AWS ####"
aws --version
#echo "###########  SQS ###########"
#aws --endpoint-url=http://localhost:4566 sqs list-queues
#aws --endpoint-url=http://localhost:4566 sqs send-message --queue-url http://localhost:4566/000000000000/local_queue --message-body "Testando cli"
#aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/local_queue
#echo "###########  s3 ###########"
#aws --endpoint-url=http://localhost:4566 s3 ls
#aws --endpoint-url=http://localhost:4566 s3 ls s3://my-bucket
#aws --endpoint-url=http://localhost:4566 s3 cp s3://my-bucket-async/6310140d-35eb-4921-8266-3eb691028632 .

#aws aws --endpoint http://localhost:4566 iam create-role --role-name lambda-execution --assume-role-policy-document '{"Version":"2012-10-17","Statement":[{"Effect":"Allow","Principal":{"Service:lambda.amazonaws.com"},"\"Action":"sts:AssumeRole"}]}'
#aws --endpoint http://localhost:4566 iam attach-role-policy --role-name lambda-execution --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
#aws --endpoint http://localhost:4566 lambda create-function --function-name HelloWorld --zip-file fileb://lambda-sqs-1.3.jar --handler br.com.Handler --runtime java11 --role arn:aws:iam::000000000000:role/lambda-execution
#aws --endpoint http://localhost:4566 lambda invoke --function-name HelloWorld out.txt --log-type Tail