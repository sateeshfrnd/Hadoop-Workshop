#!/bin/sh

option="${1}"
case ${option} in
	-check_path) hdfs_path=${2}
		if $(hadoop fs -test -d $hdfs_path) ; then 
			echo "$hdfs_path EXISTS IN HDFS"
		else 
			echo "Error:: $hdfs_path NOT EXISTS IN HDFS"
			exit 1
		fi
		;;
   *)
	echo "`basename ${0}`:usage: [-check_path <HDFS_PATH>]"
	exit 1 # Command to come out of the program with status 1
    ;;
esac

