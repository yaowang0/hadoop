package com.expgiga.mapreduce.friend;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;

public class RunJob {

    public static void main(String[] args) {
        Configuration config = new Configuration();
//        config.set("fs.defaultFS", "hdfs://node5:8020");
//        config.set("yarn.resourcemanager.hostname", "node1");
//		config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\wc.jar");
//		config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", ",");
        if (run1(config)) {
//			run2(config);
        }
    }

    public static boolean run1(Configuration config) {
        try {
            FileSystem fs = FileSystem.get(config);
            Job job = Job.getInstance(config);
            job.setJarByClass(RunJob.class);
            job.setJobName("friend");
            job.setMapperClass(FofMapper.class);
            job.setReducerClass(FofReducer.class);
            job.setMapOutputKeyClass(Fof.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setInputFormatClass(KeyValueTextInputFormat.class);

            FileInputFormat.addInputPath(job, new Path("E:/hadoop/src/data/friend"));

            Path outpath = new Path("E:/hadoop/output/friend");
            if (fs.exists(outpath)) {
                fs.delete(outpath, true);
            }
            FileOutputFormat.setOutputPath(job, outpath);

            boolean f = job.waitForCompletion(true);
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static class FofMapper extends Mapper<Text, Text, Fof, IntWritable> {

        @Override
        protected void map(Text key, Text value,
                           Context context)
                throws IOException, InterruptedException {
            String user = key.toString();
            String[] friends = StringUtils.split(value.toString(), '\t');
            for (int i = 0; i < friends.length; i++) {
                String f1 = friends[i];
                Fof ofof = new Fof(user, f1);
                context.write(ofof, new IntWritable(0));
                for (int j = i + 1; j < friends.length; j++) {
                    String f2 = friends[j];
                    Fof fof = new Fof(f1, f2);
                    context.write(fof, new IntWritable(1));
                }
            }
        }
    }

    static class FofReducer extends Reducer<Fof, IntWritable, Fof, IntWritable> {
        protected void reduce(Fof arg0, Iterable<IntWritable> arg1,
                              Context arg2)
                throws IOException, InterruptedException {
            int sum = 0;
            boolean f = true;
            for (IntWritable i : arg1) {
                if (i.get() == 0) {
                    f = false;
                    break;
                } else {
                    sum = sum + i.get();
                }
            }
            if (f) {
                arg2.write(arg0, new IntWritable(sum));
            }
        }
    }
}
