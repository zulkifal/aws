'''
S3 has a use case when account A grants account B write access to bucket owned by account A.
This is a valid use case but object ownership stays with account B and account B has unrestricted access to the data it uploaded.
Also, account A cannot use a bucket policy to grant access to objects that are not under the ownership of account A.

This sample python script is put in a lambda function, the function is invoked for every newly created object and account A over-writes
obejcts that are not owned by itself. After over-write, account A is owner of new objectes uploaded by account B.

Pre-requisites:
- Lambda function should have this policy: lambda-iam-role-policy-1.json
- When object is uploaded, bucket owner must be given Full-Control over that object via ACLs
'''
from __future__ import print_function
import json
import urllib
import boto3
import time

print('Loading function')

# boto3 reference: http://boto3.readthedocs.io/en/latest/reference/services/s3.html#client
s3 = boto3.client('s3')

def lambda_handler(event, context):
    # TODO implement
    # Get bucket & object's key names from event dict: http://docs.aws.amazon.com/AmazonS3/latest/dev/notification-content-structure.html
    bucket = event["Records"][0]["s3"]["bucket"]["name"]
    key = event["Records"][0]["s3"]["object"]["key"]
    print("bucket/object: " + bucket + "/" + key)
    # Get bucket_owner's ID
    try:
        response = s3.get_bucket_acl(Bucket=bucket)
    except:
        print("Exception found, Lambda does not have permission to execute get_bucket_acl.")
        return 1
    bucket_owner = response["Owner"]["ID"]
    print("Bucket Owner: " + bucket_owner)

    # Get object_owner's ID
    try:
        response = s3.get_object_acl(Bucket=bucket,Key=key)
    except:
        print("Exception found, Lambda does not have permission to execute get_object_acl. Check if bucket owner has been given access to object via ACLs.")
        return 1
    object_owner = response["Owner"]["ID"]
    print("Object Owner: " + object_owner)


    # Over-write Object only if ownerships do not match
    if(bucket_owner != object_owner):
        # Get object's metadata
        try:
            response = s3.head_object(Bucket=bucket,Key=key)
        except:
            print("Exception found, Lambda does not have permission to execute head_object.")
            return 1
        object_metadata = response["Metadata"]
        print("Object\'s Metadata;")
        print(object_metadata)
        print("Ownerships don't match. Overwriting object: " + bucket + "/" + key)
        # sleep for 500ms optionally
        # time.sleep(.5)
        try:
            response = s3.copy_object(Bucket=bucket,CopySource=bucket + '/' + key,Key=key,MetadataDirective='REPLACE',Metadata=object_metadata)
        except:
            print("Exception found, Lambda does not have permission to execute copy_object. Check IAM policy, object ACLs.")
            return 1
    else:
        print("Ownerships match.")

    return 0
