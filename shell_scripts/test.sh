#! /bin/bash


subj="":
body="";

#for file in `ls /errfiles/infiles/*.err`
#do
#    ${subj}=`awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $2;}' ${file}`;
#done;

cancels=(`awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $2;}' /errfiles/infiles/*.err`);
po=(`awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $5;}' /errfiles/infiles/*.err`);

#for cancel in "${cancels[@]}"; do

for (( i=0; i<${#cancels[@]}; i++)); do
    subj=${cancels[$i]};
    body=${po[$i]};

done


  #for file in /errfiles/infiles/*.err; do
#    echo ${file};
   # awk -v a="${cancel}" 'BEGIN { FS = "|" }; { if ($1=="H" && $2==a && $3=="CNC") print $5;}' ${file}
  #done


#awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print $5;}' `awk 'BEGIN { FS = "|" } ; { if ($1=="H" && $3=="CNC") print FILENAME;}'`;
