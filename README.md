# aws
Sample codes for AWS' services

This repo contains sample codes for various tasks such as converting IAM secret key to SMTP secret key when using SES' SMTP interface and obtaining encrypted emails stored in S3 bucket. These samples are not meant to be used in production, please test the code in a staging environment and make necessary changes to make it production ready.

Most of these scripts will be calling one or two APIs to solve a few particular problems that are not documented elsewhere.
 
I will keep on adding more tools as we go along.

Bash:
- SES - Test credentials and network: ses-test.sh

Python:
- SES - Convert IAM sercet to SMTP secret: ses-iam-stmp-cred.py
- S3 Cross-account Object upload - Make Object owner same as Bucket owner: s3-change-object-owner.py

PHP:
- SES - Convert IAM sercet to SMTP secret: ses-iam-stmp-cred.php
- SES - Send email with attachments via SES' RESTAPI: ses-send-raw-email.php

Java:
- S3/SES - Retrieve an encrypted email from S3 bucket: s3-get-encrypted-mail.java

Ruby:
- S3/SES Retrieve an encrypted email from S3 bucket: s3-get-encrypted-mail.rb

