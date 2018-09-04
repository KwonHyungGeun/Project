package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.goalbean;
import com.java.web.bean.losebean;
import com.java.web.bean.losegoalbean;

public class losegoalmapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		losegoalbean lgb = new losegoalbean(value);
		Text outputKey = new Text();
		if (lgb.getlosegoal() >= 0) {
			outputKey.set(lgb.getClubname());
			IntWritable outputValue = new IntWritable(lgb.getlosegoal());
			context.write(outputKey, outputValue);
		}
	}
}
