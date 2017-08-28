package com.expgiga.mapreduce.weather;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class MyKey implements WritableComparable<MyKey> {

    private int year;
    private int month;
    private double hot;

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }

    int getMonth() {
        return month;
    }

    void setMonth(int month) {
        this.month = month;
    }

    double getHot() {
        return hot;
    }

    void setHot(double hot) {
        this.hot = hot;
    }

    //判断对象是是否是同一个对象，当该对象作为输出的key时，需要比较
    @Override
    public int compareTo(MyKey o) {
        int r1 = Integer.compare(this.year, o.year);
        if (r1 == 0) {
            int r2 = Integer.compare(this.month, o.getMonth());
            if (r2 == 0) {
                return Double.compare(this.hot, o.getHot());
            } else {
                return r2;
            }
        } else {
            return r1;
        }
    }

    //序列化
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(year);
        dataOutput.writeInt(month);
        dataOutput.writeDouble(hot);
    }

    //反序列化
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.year = dataInput.readInt();
        this.month = dataInput.readInt();
        this.hot = dataInput.readDouble();
    }
}
