# Amazon SES supports two interfaces; SMTP and HTTP
# HTTP interface can be used with regular IAM access/secret key pair but SMTP interface requires special credentials
# SMTP credentails can be obtained by either going to SES console or converting existing IAM secret key to SMTP secret key
# This sample python code converts IAM secret key to SMTP secret key. Access key stays the same for HTTP and SMTP.

# Documentation: http://docs.aws.amazon.com/ses/latest/DeveloperGuide/smtp-credentials.html#smtp-credentials-console

import hmac
import hashlib
import base64

# initialize variables
key = "IAM-SECRET-KEY"
msg  = "SendRawEmail"
ver = chr(2)

# get hmac, append digest to ver and primt smtp-secret
signature = hmac.new(key, msg, hashlib.sha256).digest()
signatureAndVer = ver + signature
smtpsecret = base64.b64encode(signatureAndVer)

print(smtpsecret)
