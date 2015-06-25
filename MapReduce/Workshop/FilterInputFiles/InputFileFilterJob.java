import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class InputFileFilterJob extends Configured implements Tool{

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new Configuration(), new InputFileFilterJob(), args);
	}

	@Override
	public int run(String[] arg) throws Exception {
		getConf().set(InputFileFilter.FILE_PATTERN, arg[2]);
		
		Job job = new Job(getConf());
		job.setJobName("FilterFiles Job");
		job.setJarByClass(this.getClass());
		
		job.setMapperClass(FilterFilesMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setCombinerClass(FileFilterCombiner.class);
		
		//FileInputFormat.setInputPaths(job, new Path(arg[0]));
		FileInputFormat.setInputPathFilter(job, InputFileFilter.class);
		FileInputFormat.addInputPath(job, new Path(arg[0]));
		
		FileOutputFormat.setOutputPath(job, new Path(arg[1]));
		return job.waitForCompletion(true) == true ? 0 : -1;
	}
	
	public static class FilterFilesMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
		
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			FileSplit fileSplit = (FileSplit)context.getInputSplit();
			Text currentFileName = new Text(fileSplit.getPath().getName());
			context.write(currentFileName, NullWritable.get());
		}
	}
	
	public static class FileFilterCombiner extends Reducer<Text, NullWritable, Text, NullWritable>{
		@Override
		protected void reduce(Text key, Iterable<NullWritable> values,
				Reducer<Text, NullWritable, Text, NullWritable>.Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}

}
