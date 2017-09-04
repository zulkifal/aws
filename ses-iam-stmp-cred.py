import hmac
import hashlib
import base64

# initialize variables
key = "IAM-SECRET-KEY"
msg  = "SendRawEmail"
ver = chr(2)

# get hmac, append digest to ver and primt smtp-secret
dig = hmac.new(key, msg, hashlib.sha256).digest()
signatureAndVer = ver + dig
smtpsecret = base64.b64encode(signatureAndVer)

print(smtpsecret)
