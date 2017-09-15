/*
** For incoming emails, Amazon SES can encrypt (client-side-encryption) your mail using AWS Key Management Service (KMS) before writing it to a S3 bucket.
*
** How Amazon Simple Email Service (Amazon SES) Uses AWS KMS: http://docs.aws.amazon.com/kms/latest/developerguide/services-ses.html#services-ses-overview
* Before storing email in S3, SES sends a request for encryption data key (with proper encryption context) to KMS
* KMS returns two copies of the same key; one copy is plaintext encryption key while other copy is same encryption key in encrypted format(pay attention to this point)
* SES uses the plain key(plaintext data key) to encrypt the message and then discards this key
* SES then stores the encrypted message in S3, and also stores encrypted copy of the encryption key(data key encrypted with KMS CMK) as metadata of this object
*
** Decryption
* To decrypt the message, we will have to first fetch the encrypted email from S3
* We will also fetch the data key encrypted with KMS CMK that is stored as metadata from S3
* The encrypted data key is sent to KMS (with proper encryption context), KMS decrypts the data key
* Now that we have plaintext encryption data key, we can use it to decrypt the message/email if we know the algorithm SES used to encrypt the message/email
*
*/

package com.amazonaws.samples;
import java.io.File;

import com.amazonaws.services.s3.AmazonS3EncryptionClient;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ObjectMetadata;
public class S3GetObject {
	private static AmazonS3EncryptionClient encryptionClient;
	public static void main(String[] args) {
        String bucketName = "bucket-name"; 
        String key  = "name/path-of-encrypted-object";
        //String kms_cmk_id = "d8bb2068-3439-2342ssdkh-84a5-00d6a7fa7879";
        String kms_cmk_id = "KMS-KEY-ID";
        KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider(kms_cmk_id);
        
        encryptionClient = new AmazonS3EncryptionClient(new ProfileCredentialsProvider(), materialProvider,
                new CryptoConfiguration().withKmsRegion(Regions.US_EAST_1))
            .withRegion(Region.getRegion(Regions.US_EAST_1));
        try {
            System.out.println("Downloading an object");
            String destinationFileName = "testfile"; //local_file_name
            File file = new File(destinationFileName);
            GetObjectRequest req = new GetObjectRequest(bucketName, key);
            req.setRange(0, 31457280); // Specify Get-Range = 30MB, maximum email size
            ObjectMetadata object = encryptionClient.getObject(req, file);
            System.out.println("Object-ETag: "  + object.getETag());
       
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
