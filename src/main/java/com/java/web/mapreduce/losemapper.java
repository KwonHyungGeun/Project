package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.losebean;

public class losemapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		losebean lb = new losebean(value);
		Text outputKey = new Text();
		if (lb.getLose() >= 0) {
			outputKey.set(lb.getClubname());
			IntWritable outputValue = new IntWritable(lb.getLose());
			context.write(outputKey, outputValue);
		}
	}
}
