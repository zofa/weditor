#!/bin/bash
--------------------------------------------------------------------------------------------------------
# Purpose:
# 	To be used in cron for monitoring error files arrival and sending notification
# Deployment notes:
# 	Should be scheduled in cron using following command:
# 	30 * * * * /path/to/this/file
# to monitor each 30 minutes

--------------------------------------------------------------------------------------------------------
# algorithm

#1. Read from file previous file count. If file does not exists - create one and exit
#2. If file exists - read the number and compare with the one which exists
#3. If bigger then it was - send email
#4. Otherwise - persist number of files in file and exit

# --------------------------------------------------------------------------------
SUBJECT="New Error files arrived. Please investigate."
BODY=""
TO_EMAIL="alukyanov@kraususa.com"
IN_DIR="/errfiles/infiles"
CURRENT_COUNT=`ls $IN_DIR  | wc -l`
last_file_count="";
# --------------------------------------------------------------------------------------------------------
# routinnes goes here
send_notification ()
{
  ssmtp -s "$1" "$TO_EMAIL"  $BODY;
  echo "sending email";
}
# --------------------------------------------------------------------------------------------------------
# entry point

# checking for existing the input directory
# if [ -d $IN_DIR ]
#   then
#     echo "lookup directory does not exists - exiting.";
#     send_notification "ERROR" ;
#     exit 1;
# fi


if [ -f file_count ]
then
# reading the prev file count
while read line; do
	  last_file_count="$line";
	done < file_count

	if [ "$CURRENT_COUNT" -gt "$last_file_count" ]
	then
	  echo `ls $IN_DIR | wc -l` > file_count;
	  send_notification;
	fi
	exit 0;

else
 echo `ls $IN_DIR | wc -l` > file_count;
 exit 0;
fi
--------------------------------------------------------------------------------------------------------