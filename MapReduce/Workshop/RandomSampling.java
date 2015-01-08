package org.satish.hadoop.mapreduce;

import java.io.IOException;
import java.util.Random;

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
public class RandomSampling extends Configured implements Tool{

	public static final String SAMPLE_PERCENTAGE_VALUE = "SAMPLE_PERCENTAGE_VALUE";
	
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new RandomSampling(), args);
	}

	@Override
	public int run(String[] arg) throws Exception {
		Configuration conf = new Configuration();
		conf.set(SAMPLE_PERCENTAGE_VALUE, arg[2]);
		
		Job job = new Job(conf, "Random Sampling");
		job.setJarByClass(RandomSampling.class);
		
		job.setMapperClass(RSamplingMapper.class);
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
	
	static class RSamplingMapper extends Mapper<LongWritable, Text, NullWritable, Text>{
		private Random random = new Random();
		private Double samplePercentage;
		
		@Override
		protected void setup(Context context)
				throws IOException, InterruptedException {
			String percentage = context.getConfiguration().get(SAMPLE_PERCENTAGE_VALUE);
			samplePercentage = Double.valueOf(percentage) / 100.0;
		}
		
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Double randomValue = random.nextDouble();
			//System.out.println("samplePercentage = "+samplePercentage+" :: randomValue = "+randomValue);
			
			if (randomValue < samplePercentage) {
				context.write(NullWritable.get(), value);
			}
		}
	}
}
