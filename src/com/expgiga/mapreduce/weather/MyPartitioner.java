package com.expgiga.mapreduce.weather;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class MyPartitioner extends HashPartitioner<MyKey, DoubleWritable> {

    //执行时间越短越好，每输出一个数据就调用一次
    //需求：每一年，一个分区
    @Override
    public int getPartition(MyKey key, DoubleWritable value, int numReduceTasks) {
        return (key.getYear() - 1949) % numReduceTasks;
    }
}