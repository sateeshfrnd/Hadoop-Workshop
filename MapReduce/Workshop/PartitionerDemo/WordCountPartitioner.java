import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class WordCountPartitioner extends Partitioner<Text, LongWritable>{

	@Override
	public int getPartition(Text key, LongWritable value, int noOfReducers) {
		String tempString = key.toString();
		
		int reducerIndex = (tempString.toLowerCase().charAt(0)-'a')%noOfReducers;
		if(reducerIndex > 0 && reducerIndex < noOfReducers){
			return reducerIndex;
		}else{
			return 0;
		}
	}
	
}
