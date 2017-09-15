# aws
Sample codes for AWS' services

This repo contains sample codes for various tasks such as converting IAM secret key to SMTP secret key when using SMTP
 interface of SES.
 
I will keep on adding more and more tools as we go along.

Python:
- Convert IAM sercet to SMTP secret for SES: ses-iam-stmp-cred.py

PHP:
- Convert IAM sercet to SMTP secret for SES: ses-iam-stmp-cred.php
- Send email with attachments via SES' RESTAPI: send-raw-email.php

Java:
- Retrieve an encrypted email from S3 bucket: get-encrypted-mail.java

WARNING: These samples are not meant to be used in production, please test the code in a staging environment and make
 necessary changes to make it production ready.
