#!/bin/sh

#############################################################
# USAGE : 													
# sh hive-test.sh -check_database <DBNAME>					
# sh hive-test.sh -check_table <DBNAME> <TABLE_NAME>		
# sh hive-test.sh -check_query <QUERY>						
#############################################################

option="${1}"
case ${option} in
	-check_database) databasename=${2}
           validateTable=$(hive -S -e "SHOW DATABASES LIKE '$databasename'")
           if [[ -z $validateTable ]]; then
				echo "Error:: $databasename CANNOT BE FOUNED"
				exit 1
		   else 
				echo "$databasename FOUND"
           fi
           ;;
		   
   -check_table) databasename=${2}
		   table=${3}
           validateTable=$(hive --database $databasename -S -e "SHOW TABLES LIKE '$table'")
           if [[ -z $validateTable ]]; then
			   echo "Error:: $table CANNOT BE FOUNED"
			   exit 1
		   else
			   echo "$table FOUND"
           fi
           ;;
   
   -check_query) query=${2}
           validateTable=$(hive -S -e "query")
           if [[ -z $validateTable ]]; then
           echo "Error:: $table cannot be found"
           exit 1
           fi
           ;;
   *)
      echo "`basename ${0}`:usage: [-check_database <DBNAME>] | [-check_table <DBNAME> <TABLE_NAME>] | [-check_query <QUERY>]"
      exit 1 # Command to come out of the program with status 1
      ;;
esac
