=begin
* For incoming emails, Amazon SES can encrypt (client-side-encryption) your mail using AWS Key Management Service (KMS) before writing it to a S3 bucket.

** How Amazon Simple Email Service (Amazon SES) Uses AWS KMS: http://docs.aws.amazon.com/kms/latest/developerguide/services-ses.html#services-ses-overview
* Before storing email in S3, SES sends a request for encryption data key (with proper encryption context) to KMS
* KMS returns two copies of the same key; one copy is plaintext encryption key while other copy is same encryption key in encrypted format(pay attention to this point)
* SES uses the plain key(plaintext data key) to encrypt the message and then discards this key
* SES then stores the encrypted message in S3, and also stores encrypted copy of the encryption key(data key encrypted with KMS CMK) as metadata of this object

** Decryption
* To decrypt the message, we will have to first fetch the encrypted email from S3
* We will also fetch the data key encrypted with KMS CMK that is stored as metadata from S3
* The encrypted data key is sent to KMS (with proper encryption context), KMS decrypts the data key
* Now that we have plaintext encryption data key, we can use it to decrypt the message/email if we know the algorithm SES used to encrypt the message/email

=end
require 'aws-sdk'
bucket_name = "bucket-name"
key = "object-key"
s3_new = Aws::S3::Encryption::Client.new(kms_key_id:'CMK-Id', region: 'us-east-1')
resp = s3_new.get_object(
  	bucket: bucket_name,
  	key: key)
puts "Downloaded '%s' from S3!" % key
File.open(key, 'w') { |file| file.write(resp.body.read) }
