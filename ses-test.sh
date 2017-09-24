#!/usr/bin/env bash
# Note: This script should be used with caution, the output of this script contains SMTP credentials in clear text.
# Before sharing logs with anyone, please remove or redact SMTP secret/password

# This script can be used to test network, smtp credentials and any possible limits on your SES account (such as sandbox mode)
# it uses curl's SMTP capabilities and we can avoid SMTP timeouts that are difficult to deal with when using telnet or 
# openssl's s_client

# check for number of arguments
if [ $# -lt 4 ]
then
	echo "Usage: $0 REGION access-key secret-key test@verified-email-address.com"
	echo "OR"
	echo "Usage: $0 REGION access-key secret-key test@verified-email-address.com to@email-address.com"
	echo "REGION: us-east-1|us-west-2|eu-west-1"
	echo "secret-key: it must be smtp-secret key, not IAM secret key"
	exit 1
fi

# check if curl is installed
type curl >/dev/null 2>&1 || { echo >&2 "I require curl but it's not installed.  Aborting."; exit 1; }

# create sample email
echo "From: $4" > /tmp/ses-test-message
echo "To: success@simulator.amazonses.com" >> /tmp/ses-test-message
echo "Subject: Email from SES test Script" >> /tmp/ses-test-message
echo "" >> /tmp/ses-test-message
echo "Hi, this is a test email from bash script." >> /tmp/ses-test-message

# check if to: email address is set
if [ ! -z $5 ]
then
	# send to provided destination email address
	curl -v --mail-from $4 --mail-rcpt $5 --user $2':'$3 'smtps://email-smtp.'$1'.amazonaws.com:465' -T /tmp/ses-test-message
else
	# send to success@simulator.amazonses.com
	curl -v --mail-from $4 --mail-rcpt 'success@simulator.amazonses.com'  --user $2':'$3 'smtps://email-smtp.'$1'.amazonaws.com:465' -T /tmp/ses-test-message
fi
