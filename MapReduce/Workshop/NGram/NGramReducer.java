package com.satish.mapreduce.workshop.ngram;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NGramReducer extends Reducer<Text, IntWritable, Text, LongWritable>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> value,
			Reducer<Text, IntWritable, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {
		long sum = 0;
		while (value.iterator().hasNext()) {
			sum += value.iterator().next().get();
		}
		context.write(key, new LongWritable(sum));
	}
}
