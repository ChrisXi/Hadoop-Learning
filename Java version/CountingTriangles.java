package hw3;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class CountingTriangles {

    public static class Map extends MapReduceBase implements
            Mapper<LongWritable, Text, Text, IntWritable> {
    	private Text K = new Text();
		private final static IntWritable V = new IntWritable(1);


        public void map(LongWritable key, Text value,
                OutputCollector<Text, IntWritable> output, Reporter reporter)
                throws IOException {
			String ID = value.toString();// text to String
			// System.out.println("studentInfo:"+stuInfo);

			StringTokenizer itr = new StringTokenizer(ID);

			// store Origianl ID
			String Original_ID = itr.nextToken();
			// store all the friends' ID
			ArrayList<String> Friends_ID = new ArrayList<String>();

			while (itr.hasMoreTokens()) {
				String friends_ID = itr.nextToken();// 
				Friends_ID.add(friends_ID);
			}

			int nlen = Friends_ID.size();

			for (int i = 0; i < nlen - 2; i++) {
				for (int j = i + 1; j < nlen - 1; j++) {
					int v0 = Integer.parseInt(Original_ID);
					int v1 = Integer.parseInt(Friends_ID.get(i));
					int v2 = Integer.parseInt(Friends_ID.get(j));

					String strV;
					if (v1 < v2)// original key:v0
						strV = Original_ID + "," + Friends_ID.get(i) + ","
								+ Friends_ID.get(j);
					else
						strV = Original_ID + "," + Friends_ID.get(j) + ","
								+ Friends_ID.get(i);
					K.set(strV);
					output.collect(K, V);

					if (v0 < v2)// original key:v1
						strV = Friends_ID.get(i) + "," + Original_ID + ","
								+ Friends_ID.get(j);
					else
						strV = Friends_ID.get(i) + "," + Friends_ID.get(j)
								+ "," + Original_ID;
					K.set(strV);
					output.collect(K, V);

					if (v0 < v1)// original key:v2
						strV = Friends_ID.get(j) + "," + Original_ID + ","
								+ Friends_ID.get(i);
					else
						strV = Friends_ID.get(j) + "," + Friends_ID.get(i)
								+ "," + Original_ID;
					K.set(strV);
					output.collect(K, V);

				}
			}
		}
    }

    public static class Reduce extends MapReduceBase implements
            Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterator<IntWritable> values,
                OutputCollector<Text, IntWritable> output, Reporter reporter)
                throws IOException {
        	int sum = 0;
			while (values.hasNext()) {
				sum += 1;
				values.next().get();
			}
			output.collect(key, new IntWritable(sum));
        }
    }

	
	public static class Map1 extends MapReduceBase implements
			Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text triangle = new Text();

		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			StringTokenizer itr = new StringTokenizer(line);
			//first value:triangle
			//second value:count
			
			if (itr.hasMoreTokens()) {
				triangle.set(itr.nextToken());
			}
			if (itr.hasMoreTokens()){
				String strCount = itr.nextToken();
				int count = Integer.parseInt(strCount);
				if(count == 3)
					output.collect(triangle, one);
			}
		}
	}
	
	public static class Reduce1 extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			
			output.collect(key, null);
		}
	}
	/**/
    
    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf(CountingTriangles.class);
        conf.setJobName("wordcount");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(Map.class);
        //conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path("temp"));

        JobClient.runJob(conf);
        
  		JobConf conf2 = new JobConf(CountingTriangles.class);
		conf2.setJobName("outputTriangles");

		conf2.setOutputKeyClass(Text.class);
		conf2.setOutputValueClass(IntWritable.class);

		conf2.setMapperClass(Map1.class);
		//conf.setCombinerClass(Reduce1.class);
		conf2.setReducerClass(Reduce1.class);

		conf2.setInputFormat(TextInputFormat.class);
		conf2.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf2, new Path("temp"));
		FileOutputFormat.setOutputPath(conf2, new Path(args[1]));

		JobClient.runJob(conf2);/**/
    }
}
