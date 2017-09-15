<?php

// Amazon SES supports two interfaces; SMTP and HTTP
// HTTP interface can be used with regular IAM access/secret key pair but SMTP interface requires special credentials
// SMTP credentails can be obtained by either going to SES console or converting existing IAM secret key to SMTP secret key
// This sample php code converts IAM secret key to SMTP secret key. Access key stays the same for HTTP and SMTP.

// Documentation: http://docs.aws.amazon.com/ses/latest/DeveloperGuide/smtp-credentials.html#smtp-credentials-console

// Initialize variables
$secret = "IAM-SECRET-KEY";
$message = "SendRawEmail";
$ver = chr(2);

// get hmac, append digest to ver and primt smtp-secret
$signatureInBytes = hash_hmac('sha256', $message, $secret, true);
$signatureAndVer = $ver.$signatureInBytes;
$smtpSecret = base64_encode($signatureAndVer);

echo "SMTP Secret: ".$smtpSecret.PHP_EOL;

?>
