package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.rankbean;

public class rankmapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		rankbean rk = new rankbean(value);
		Text outputKey = new Text();
		if (rk.getRank() > 0) {
			outputKey.set(rk.getClubname() + rk.getRank());
			IntWritable outputValue = new IntWritable(1);
			context.write(outputKey, outputValue);
		}
	}

}
