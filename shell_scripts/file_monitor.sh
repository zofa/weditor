#!/bin/bash -e
# set -x
# --------------------------------------------------------------------------------------------------------
# Purpose:
# 	To be used in cron for monitoring error files arrival and sending notification
# Deployment notes:
# 	Should be scheduled in cron using following command:
# 	30 * * * * /path/to/this/file
# to monitor each 30 or less minutes

# to avoid home directory issues.
cd $(dirname $0)

#
# *********** CHANGE HISTORY *************
# 8/4/13  -- added sending mail notification for order cancellation records

# --------------------------------------------------------------------------------------------------------
# algorithm

#1. Read from file previous file count. If file does not exists - create one and exit
#2. If file exists - read the number and compare with the one which exists
#3. If bigger then it was - send email
#4. Otherwise - persist number of files in file and exit
# Same algorithm used for counting cancel records.
# --------------------------------------------------------------------------------
IN_DIR="/errfiles/infiles"
CURRENT_COUNT=`ls ${IN_DIR}  | wc -l`
last_file_count="";
app_link="http://fixedi.kraususa.net:8080/";

# last num of canceled records.
LAST_CANCEL_ORDER_NUM=""; 

# --------------------------------------------------------------------------------------------------------
TO_EMAIL="kbedi@kraususa.com" # orders@kraususa.com  kbedi
SUBJECT="New Error files arrived. Please investigate."
BODY=""
# --------------------------------------------------------------------------------------------------------
# routines goes here

send_notification ()
{
   echo "About to send email with Subject $1 to $TO_EMAIL";

which `sendmail` ${TO_EMAIL}  << EOF
From: Notifitor <notification@kraususa.com>
To: Nitification receipients <notifications@kraususa.com>
Subject: $1
--------------------------------------------------------------------------------------------------
${BODY}
--------------------------------------------------------------------------------------------------
Sent from EDI Fix Tool from host: `hostname`
Please do not reply to this automatic generated email.
EOF
}
# --------------------------------------------------------------------------------------------------------
# ENTRY POINT

# checking for existing the input directory
if [ ! -d "${IN_DIR}" ]
   then
     BODY="I cannot find input directory for errors lookup: ${IN_DIR} at `hostname`";
     send_notification "ERROR" ;
     exit 1;
fi


if [ -f file_count ]
then
# reading the prev file count
while read line; do
	  last_file_count="$line";
	done < file_count

	if [ "$CURRENT_COUNT" -gt "$last_file_count" ]
	then
	  echo `ls ${IN_DIR} | wc -l` > file_count;
	  BODY="Please go to link below to fix the files: ${app_link}";
	  send_notification "New Error files arrived [please fix]";
	fi
else
 echo `ls ${IN_DIR} | wc -l` > file_count;
fi
# --------------------------------------------------------------------------------------------------------
# counting number of cancellation records
# --------------------------------------
if [ -f cancels_count ]
then
 while read line; do
   LAST_CANCEL_ORDER_NUM="$line";
 done < cancels_count;

CURRENT_CANCELS=`grep -R "CNC" ${IN_DIR} | wc -l`;
	if [ ${CURRENT_CANCELS} -gt ${LAST_CANCEL_ORDER_NUM} ]
	   then
        cancels=(`awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $2;}' ${IN_DIR}/*err`);
        pns=(`awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $5;}' ${IN_DIR}/*err`);
        for (( i=0; i<${#cancels[@]}; i++)); do
            BODY=" Hi there,

There are сancellation request received with following details

                  Dealer: ${cancels[$i]}
                  PO# = ${pns[$i]}
                  date: `date +"%D"`

Please use the link ${app_link} to fix them.";
		    send_notification "Order cancellation received from [${cancels[$i]} | PO#=${pns[$i]}]";
        done
	echo `grep -R "CNC" ${IN_DIR} | wc -l` > cancels_count;
	fi
else
  echo `grep -R "CNC" ${IN_DIR} | wc -l` > cancels_count;
fi