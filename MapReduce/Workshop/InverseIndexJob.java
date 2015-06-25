package com.satish.mapreduce.workshop.inverseindex;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class InverseIndexJob implements Tool{

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new InverseIndexJob(), args);
	}

	public static class InverseIndexMapping extends
			Mapper<Object, Text, Text, Text> {

		@Override
		protected void map(Object key, Text value,
				Context context)
				throws IOException, InterruptedException {
			FileSplit fileSplit = (FileSplit)context.getInputSplit();
			Text currentFileName = new Text(fileSplit.getPath().getName());
			System.out.println("File Name = "+currentFileName.toString());
			
			StringTokenizer tokenizer = new StringTokenizer(value.toString());
			while(tokenizer.hasMoreTokens()){
				context.write(new Text(tokenizer.nextToken()), currentFileName);
			}
		}
		
	}

	public static class InverseIndexReducer extends
		Reducer<Text, Text, Text, Text>{
		
		@Override
		protected void reduce(Text key, Iterable<Text> value,
				Context context)
				throws IOException, InterruptedException {
			Vector<String> fileList = new Vector<String>();
			while (value.iterator().hasNext()) {
				fileList.add(value.iterator().next().toString());
			}
			context.write(new Text(key), new Text(fileList.toString()));
		}
	}

	private Configuration conf;

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public int run(String[] args) throws Exception {
		Job wordCountJob = new Job(getConf());
		wordCountJob.setJobName("InverseIndexing Job");
		wordCountJob.setJarByClass(this.getClass());
		wordCountJob.setMapperClass(InverseIndexMapping.class);
		wordCountJob.setReducerClass(InverseIndexReducer.class);
		wordCountJob.setMapOutputKeyClass(Text.class);
		wordCountJob.setMapOutputValueClass(Text.class);
		wordCountJob.setOutputKeyClass(Text.class);
		wordCountJob.setOutputValueClass(Text.class);
		FileInputFormat.setInputPaths(wordCountJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(wordCountJob, new Path(args[1]));
		return wordCountJob.waitForCompletion(true) == true ? 0 : -1;
	}
}
