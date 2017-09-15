# aws
Sample codes for AWS' services

This repo contains sample codes for various tasks such as converting IAM secret key to SMTP secret key when using SES' SMTP interface and obtaining encrypted emails stored in S3 bucket. These samples are not meant to be used in production, please test the code in a staging environment and make necessary changes to make them production ready.

Most of these scripts will be calling one or two APIs to solve a few particular problems that are not documented elsewhere.
 
I will keep on adding more and more tools as we go along.

Python:
- Convert IAM sercet to SMTP secret for SES: ses-iam-stmp-cred.py

PHP:
- Convert IAM sercet to SMTP secret for SES: ses-iam-stmp-cred.php
- Send email with attachments via SES' RESTAPI: send-raw-email.php

Java:
- Retrieve an encrypted email from S3 bucket: get-encrypted-mail.java

Ruby:
- Retrieve an encrypted email from S3 bucket: get-encrypted-mail.rb

WARNING: These samples are not meant to be used in production, please test the code in a staging environment and make
 necessary changes to make them production ready.
