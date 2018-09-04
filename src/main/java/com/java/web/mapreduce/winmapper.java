package com.java.web.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.java.web.bean.winbean;

public class winmapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		winbean wb = new winbean(value);
		Text outputKey = new Text();
		if (wb.getWin() >= 0) {
			outputKey.set(wb.getClubname());
			IntWritable outputValue = new IntWritable(wb.getWin());
			context.write(outputKey, outputValue);
		}
	}
}
