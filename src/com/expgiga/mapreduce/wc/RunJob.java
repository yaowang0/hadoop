package com.expgiga.mapreduce.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class RunJob {

    public static void main(String[] args) {
        Configuration config = new Configuration();
//        config.set("fs.defaultFS", "hdfs://node1:8020");
//        config.set("yarn.resourcemanager.hostname", "node1");
//        config.set("mapred.jar", "C:\\Users\\Administrator\\Desktop\\wc.jar");

        try {
            FileSystem fs = FileSystem.get(config);

            Job job = Job.getInstance(config); //会将src或者classPath路径下的配置文件加载
            job.setJarByClass(RunJob.class);

            job.setJobName("wc");

            job.setMapperClass(WordCountMapper.class);
            job.setReducerClass(WordCountReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path("E:/hadoop/src/data/wc.txt"));

            Path outpath = new Path("E:/hadoop/output/wordcout");
            if (fs.exists(outpath)) {
                fs.delete(outpath, true);
            }
            FileOutputFormat.setOutputPath(job, outpath);

            boolean f = job.waitForCompletion(true);
            if (f) {
                System.out.println("Doing the Job Task!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
