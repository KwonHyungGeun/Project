package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.playedbean;


public class playedmapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		playedbean pb = new playedbean(value);
		Text outputKey = new Text();
		if (pb.getPlayed() >= 0) {
			outputKey.set(pb.getClubname());
			IntWritable outputValue = new IntWritable(pb.getPlayed());
			context.write(outputKey, outputValue);
		}
	}
}
