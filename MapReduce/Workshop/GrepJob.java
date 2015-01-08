package org.satish.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author satishkumar
 *
 */
public class GrepJob extends Configured implements Tool{

	public static final String GREP_PATTERN = "GREP_PATTERN";
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new GrepJob(), args);
	}

	@Override
	public int run(String[] arg) throws Exception {
		Configuration conf = new Configuration();
		conf.set(GREP_PATTERN, arg[2]);
		
		Job job = new Job(conf, "Grep Job");
		job.setJarByClass(GrepJob.class);
		
		job.setMapperClass(GrepMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Text.class);
		
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
	
	static class GrepMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
		private String grepPattern;
		
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			grepPattern = context.getConfiguration().get(GREP_PATTERN);
		}
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			//System.out.println("grepPattern = "+grepPattern);
			
			if (value.toString().matches(".*"+grepPattern+".*")) {
				context.write(NullWritable.get(), value);
			}
		}
	}
}
