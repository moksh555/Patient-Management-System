#!/bin/bash
set -e # Stops the script if any command fails
#
## set creds + region for LocalStack
#export AWS_ACCESS_KEY_ID=test
#export AWS_SECRET_ACCESS_KEY=test
#export AWS_DEFAULT_REGION=us-east-1
##
### set the endpoint var (this is the one you’re missing)
#export EP=http://localhost:4566
##
## sanity-check
#echo "$EP"   # should print http://localhost:4566
##
#aws --endpoint-url="$EP" logs describe-log-groups \
#  --log-group-name-prefix "/ecs/" \
#  --query 'logGroups[].logGroupName' --output table
#
#aws --endpoint-url="$EP" logs delete-log-group --log-group-name "/ecs/api-gateway"
#
#aws --endpoint-url=http://localhost:4566 cloudformation delete-stack \
#    --stack-name patient-management

aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"

aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text