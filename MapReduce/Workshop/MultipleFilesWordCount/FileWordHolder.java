package com.satish.hadoop.mapreduce.fileswordcount;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

/**
 * @author satishkumar
 *
 */
public class FileWordHolder implements WritableComparable<FileWordHolder>{

	private String fileName;
	private String word;
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public FileWordHolder(){
		
	}
	
	public FileWordHolder(String fileName, String word){
		this.fileName = fileName;
		this.word = word;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		fileName = in.readUTF();
		word = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(fileName);
		out.writeUTF(word);		
	}

	@Override
	public int compareTo(FileWordHolder fileWordHolder) {
		int diff = this.fileName.compareTo(fileWordHolder.fileName);
		if(diff == 0){
			diff = this.word.compareTo(fileWordHolder.word);
		}
		return diff;
	}
	
	@Override
	public String toString() {
		return this.fileName+" :: "+this.word;
	}
	
	@Override
	public int hashCode() {
		final int hash = 11;
		int result = 1;
		result = hash * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = hash * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(this == obj){
			return true;
		}
		
		if(obj == null){
			return false;
		}
		
		if(getClass() != obj.getClass()){
			return false;
		}
		
		FileWordHolder fileWordHolder = (FileWordHolder)obj;
		if(this.fileName == null){
			if(fileWordHolder.fileName != null){
				return false;
			}
		} else if (!this.fileName.equals(fileWordHolder.fileName)){
			return false;
		}
			
		if(this.word == null){
			if(fileWordHolder.word != null){
				return false;
			}
		} else if (!this.word.equals(fileWordHolder.word)){
			return false;
		}
		
		return true;
	}
}
