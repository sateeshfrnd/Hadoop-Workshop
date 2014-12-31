package com.satish.hadoop.mapreduce.fileswordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author satishkumar
 *
 */
public class MultipleFilesWordCountJob extends Configured implements Tool{

	public static void main(String[] args) throws Exception {
		int result = ToolRunner.run(new MultipleFilesWordCountJob(), args);
		System.exit(result);
	}

	@Override
	public int run(String[] arg) throws Exception {
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "Finding word count in all files");
		job.setJarByClass(MultipleFilesWordCountJob.class);
		
		job.setMapperClass(FileWordCountMapper.class);
		job.setMapOutputKeyClass(FileWordHolder.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setCombinerClass(FileWordCountCombiner.class);
		
		job.setReducerClass(FileWordCountReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.addInputPath(job, new Path(arg[0]));
		FileOutputFormat.setOutputPath(job,removeIfExitAndSetOutputPath(conf, arg[1]));
		
		return (job.waitForCompletion(true) ? 0 : 1);
	}
	
	private Path removeIfExitAndSetOutputPath(Configuration conf,String path) throws IOException{
		FileSystem fileSystem = FileSystem.get(conf);
		Path outputPath = new Path(path);
		fileSystem.delete(outputPath);
		return outputPath;
	}
	
	static class FileWordCountMapper extends Mapper<LongWritable, Text, FileWordHolder, IntWritable>{
		
		@Override
		protected void map(LongWritable key, Text value,Context context)
				throws IOException, InterruptedException {
			StringTokenizer tokenizer = new StringTokenizer(value.toString());
			
			while(tokenizer.hasMoreTokens()){
				String fileName = getFileName(context);
				String word = tokenizer.nextToken().toString();
				word.replace('.', ' ').trim();
				word.replace(',', ' ').trim();
				context.write(new FileWordHolder(fileName, word), new IntWritable(1));
			}
		}
		
		public String getFileName(Context context){
			String fileName ="NA";
			FileSplit fileSplit = (FileSplit)context.getInputSplit();
			if(fileSplit != null){
				fileName = fileSplit.getPath().getName();
			}
			return fileName;
		}
	}
	
	static class FileWordCountCombiner extends Reducer<FileWordHolder, IntWritable, FileWordHolder, IntWritable>{
		@Override
		protected void reduce(FileWordHolder fileWordHolder, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable value : values){
				sum += value.get();
			}
			context.write(fileWordHolder, new IntWritable(sum));
		}
	}
	
	static class FileWordCountReducer extends Reducer<FileWordHolder, IntWritable, Text, NullWritable>{
		
		private MultipleOutputs<Text, IntWritable> mouts;
		@Override
		protected void setup(org.apache.hadoop.mapreduce.Reducer.Context context)
				throws IOException, InterruptedException {
			mouts = new MultipleOutputs<Text, IntWritable>(context);
		}
		
		@Override
		protected void reduce(FileWordHolder fileWordHolder, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable value : values){
				sum += value.get();
			}
			String output = fileWordHolder.toString() +" = "+sum;
			context.write(new Text(output), NullWritable.get());
		}
		
		@Override
		protected void cleanup(Context context)
				throws IOException, InterruptedException {
			mouts.close();
		}
	}
}
