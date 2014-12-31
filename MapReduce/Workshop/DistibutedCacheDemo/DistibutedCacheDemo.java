package com.satish.hadoop.mapreduce.distributedcache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
public class DistibutedCacheDemo extends Configured implements Tool{


	@Override
	public int run(String[] arg) throws Exception {
		Configuration conf = new Configuration();
		
		Job job = new Job(conf, "DistibutedCacheDemo - Get the User and accessed Page Name");
		job.setJarByClass(DistibutedCacheDemo.class);
		
		DistributedCache.addCacheFile(new Path(arg[0]).toUri(), job.getConfiguration());
//		DistributedCache.setLocalFiles(job.getConfiguration(), arg[0]);
		
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		FileInputFormat.addInputPath(job, new Path(arg[1]));
		FileOutputFormat.setOutputPath(job, removeIfExitAndSetOutputPath(job.getConfiguration(), arg[2]));
		
		return (job.waitForCompletion(true) ? 0 : 1);
	}
	
	public static void main(String[] args) throws Exception {
		int result = ToolRunner.run(new DistibutedCacheDemo(), args);
		System.exit(result);
	}
	
	private Path removeIfExitAndSetOutputPath(Configuration conf,String path) throws IOException{
		FileSystem fileSystem = FileSystem.get(conf);
		Path outputPath = new Path(path);
		fileSystem.delete(outputPath);
		return outputPath;
	}
	
	static class MyMapper extends Mapper<LongWritable, Text, Text, Text>{
		
		Path[] cachefiles = new Path[0];	//	To store the path of the cache file.
		
		Hashtable<String, String> lookupdata = new Hashtable<String, String>();
		
		@Override
		protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
				throws IOException, InterruptedException {
			Configuration conf = context.getConfiguration();
			
			cachefiles = DistributedCache.getLocalCacheFiles(conf);
			if(cachefiles == null){
				System.out.println("No Cache files");
				return;
			}
			BufferedReader reader = new BufferedReader(new FileReader(cachefiles[0].toString()));
			String line;
			while((line = reader.readLine()) != null){
				String[] lineData = line.split("\t");
				if(lineData.length != 0 && lineData.length == 2){
					lookupdata.put(lineData[0].trim(), lineData[1]);
				}
			}
		}
		
		@Override
		protected void map(LongWritable key, Text value,
				org.apache.hadoop.mapreduce.Mapper.Context context)
				throws IOException, InterruptedException {
			String[] lineData = value.toString().split("\t");
			String pageName = lookupdata.get(lineData[1].trim());
			if(pageName != null){
				context.write(new Text(lineData[0]), new Text(pageName));
			}
		}
		
	}

}
