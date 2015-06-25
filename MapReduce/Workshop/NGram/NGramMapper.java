package com.satish.mapreduce.workshop.ngram;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class NGramMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	int N;
	@Override
	protected void setup(
			Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		N = context.getConfiguration().getInt(NGramJob.N_VALUE, 0);
	}
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		String[] words = value.toString().split("[ ]");
		for(int i = 0 ; i < words.length ; i++){
			String first = words[i];
			StringBuffer nStrings = new StringBuffer();
			for( int j = i ; (j < i + N) && (i+N <= words.length); j++){
				nStrings.append(words[j]);
				nStrings.append(" ");
			}
			context.write(new Text(nStrings.toString()), new IntWritable(1));
		}
	}
}
