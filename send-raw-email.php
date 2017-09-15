<?php
// SES provides two RESTAPIs when it comes to sending emails. The simpler SendEmail API takes care of email formatting
// but does not support attachments. SendRawEmail API can be used to send email to overcome that limitation but it requires
// email formatting on client side.

// This sample script uses PHPMailer to take care of email formating and then uses php-sdk for aws to call SendRawEmail API
// SendRawEmail with PHP: http://docs.aws.amazon.com/aws-sdk-php/v3/api/api-email-2010-12-01.html#sendrawemail

// The following commands were ran on an EC2-instance to prepare this test environment
// $ sudo yum install php56 -y
// $ curl -sS https://getcomposer.org/installer | php
// $ php composer.phar require aws/aws-sdk-php
// $ php composer.phar require phpmailer/phpmailer
// $aws configure # it can be skipped if EC2 instance already has a proper role attached to it

require 'vendor/autoload.php';
$mail = new PHPMailer;
// Set From: Address
$mail->From = "test@verifieddomain.com";
// Set To: Address
$mail->addAddress("success@simulator.amazonses.com", "Jeff");
//$mail->addReplyTo("replyto@test.com", "Recepient Name");
//$mail->addCc("test-cc@test.com", "Recepient Name");
//$mail->addBcc("test-bcc@ test.com", "Recepient Name");
// Path to Attachment
$mail->AddAttachment("/path/to/file");
$mail->isHTML(true);
$mail->Subject = "Subject Text";
$mail->Body = "<i>Mail body in HTML</i>";
$mail->AltBody = "This is the plain text version of the email content";

// Generating MIME without actually sending email
$mail->preSend();

// Get Generated MIME
$data=$mail->getSentMIMEMessage();

/* AWS SDK Part */
/* Instantiate a default S3Client*/
$client = new Aws\Ses\SesClient([
    'version' => 'latest',
    'region'  => 'us-east-1',
]);
$result = $client->sendRawEmail(array(
    'Source' => 'test@verifieddomain.com', //MAIL FROM: Address
    'Destinations' => array('success@simulator.amazonses.com'), // RCPT TO: Addresses
    // RawMessage is required
    'RawMessage' => array(
        // Data is required
        'Data' => $data,
    ),
));
echo $result["MessageId"] . "\n";
?>
