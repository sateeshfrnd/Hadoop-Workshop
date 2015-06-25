import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class InputFileFilter extends Configured implements PathFilter{

	Configuration conf;
	Pattern pattern;
	FileSystem fileSystem;
	public static final String FILE_PATTERN = "FILE-PATTERN";
	
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
		if(conf != null){
			try {
				fileSystem = FileSystem.get(conf);
				pattern = Pattern.compile(conf.get(FILE_PATTERN));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public boolean accept(Path path) {
		System.out.println("FILE_PATTERN = "+conf.get(FILE_PATTERN));
		try {
			if(fileSystem.isDirectory(path)){
				return true;
			}else{
				Matcher matcher = pattern.matcher(path.toString());
				System.out.println(path.toString()+" = "+matcher.matches());
				return matcher.matches();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
