package com.satish.mapreduce.workshop.ngram;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class NGramJob implements Tool {

	private Configuration configuration;
	public static String N_VALUE = "N-GRAM-VALUE";
	
	@Override
	public Configuration getConf() {
		return this.configuration;
	}

	@Override
	public void setConf(Configuration con) {
		this.configuration = con;
	}

	@Override
	public int run(String[] arg) throws Exception {
		configuration.setInt(N_VALUE, Integer.parseInt(arg[2]));
		
		Job ngramJob = new Job(getConf());
		ngramJob.setJobName("N-Gram Job");
		ngramJob.setJarByClass(this.getClass());
		
		ngramJob.setMapperClass(NGramMapper.class);
		ngramJob.setMapOutputKeyClass(Text.class);
		ngramJob.setMapOutputValueClass(IntWritable.class);
		
		ngramJob.setReducerClass(NGramReducer.class);
		ngramJob.setOutputKeyClass(Text.class);
		ngramJob.setOutputValueClass(LongWritable.class);
		
		FileInputFormat.setInputPaths(ngramJob, new Path(arg[0]));
		FileOutputFormat.setOutputPath(ngramJob, new Path(arg[1]));
		
		return ngramJob.waitForCompletion(true) == true ? 0 : -1;
	}

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new NGramJob(), args);
	}
	
}
