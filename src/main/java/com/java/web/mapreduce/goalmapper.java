package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.goalbean;
import com.java.web.bean.losebean;

public class goalmapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		goalbean gb = new goalbean(value);
		Text outputKey = new Text();
		if (gb.getGoal() >= 0) {
			outputKey.set(gb.getClubname());
			IntWritable outputValue = new IntWritable(gb.getGoal());
			context.write(outputKey, outputValue);
		}
	}
}
