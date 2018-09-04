package com.java.web.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.springframework.stereotype.Service;

import com.java.web.mapreduce.goalmapper;
import com.java.web.mapreduce.goalreduce;
import com.java.web.mapreduce.losegoalmapper;
import com.java.web.mapreduce.losegoalreduce;
import com.java.web.mapreduce.losemapper;
import com.java.web.mapreduce.losereduce;
import com.java.web.mapreduce.playedmapper;
import com.java.web.mapreduce.playedreduce;
import com.java.web.mapreduce.rankmapper;
import com.java.web.mapreduce.rankreduce;
import com.java.web.mapreduce.toprankmapper;
import com.java.web.mapreduce.toprankreduce;
import com.java.web.mapreduce.winmapper;
import com.java.web.mapreduce.winreduce;

import net.sf.json.JSONObject;

@Service
public class CommonService implements CommonServiceInterface {
	
	@Resource(name="hdConf")
	Configuration conf;
	
	HashMap<String, Object> resultMap;

	@Override
	public HashMap<String, Object> winrate(HttpServletRequest req) throws Exception {
		String league = req.getParameter("league");
		String inputpath = "/input/csv/" + league + "/" + league + ".csv";
		
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String realDate = date.format(today);
		
		Job playedjob = Job.getInstance(conf, "played");
		Job losejob = Job.getInstance(conf, "lose");
		Job winjob = Job.getInstance(conf, "win");
		
		
		URI playedinputUri = URI.create(inputpath); 
		URI playedoutputUri = URI.create("/result/" + league + "/played/" + realDate);
		String playedpath = "/result/" + league + "/played/" + realDate;
		
		URI loseinputUri = URI.create(inputpath); 
		URI loseoutputUri = URI.create("/result/" + league + "/lose/" + realDate);
		String losepath = "/result/" + league + "/lose/" + realDate;
		
		URI wininputUri = URI.create(inputpath); 
		URI winoutputUri = URI.create("/result/" + league + "/win/" + realDate);
		String winpath = "/result/" + league + "/win/" + realDate;
		
		FileInputFormat.addInputPath(playedjob, new Path(playedinputUri)); 		   
		FileOutputFormat.setOutputPath(playedjob, new Path(playedoutputUri));
		
		FileInputFormat.addInputPath(losejob, new Path(loseinputUri)); 		   
		FileOutputFormat.setOutputPath(losejob, new Path(loseoutputUri));
		
		FileInputFormat.addInputPath(winjob, new Path(wininputUri)); 		   
		FileOutputFormat.setOutputPath(winjob, new Path(winoutputUri));
		
		playedjob.setInputFormatClass(TextInputFormat.class);	
		playedjob.setOutputFormatClass(TextOutputFormat.class); 
		
		losejob.setInputFormatClass(TextInputFormat.class);	
		losejob.setOutputFormatClass(TextOutputFormat.class);
		
		winjob.setInputFormatClass(TextInputFormat.class);	
		winjob.setOutputFormatClass(TextOutputFormat.class);
		
		playedjob.setOutputKeyClass(Text.class);
		playedjob.setOutputValueClass(IntWritable.class);
		
		
		losejob.setOutputKeyClass(Text.class);
		losejob.setOutputValueClass(IntWritable.class);
		
		winjob.setOutputKeyClass(Text.class);
		winjob.setOutputValueClass(IntWritable.class);
		
		playedjob.setJarByClass(this.getClass()); 
		playedjob.setMapperClass(playedmapper.class);
		playedjob.setReducerClass(playedreduce.class);
		
		losejob.setJarByClass(this.getClass()); 
		losejob.setMapperClass(losemapper.class);
		losejob.setReducerClass(losereduce.class);
		
		winjob.setJarByClass(this.getClass()); 
		winjob.setMapperClass(winmapper.class);
		winjob.setReducerClass(winreduce.class);
		
		playedjob.waitForCompletion(true);
		losejob.waitForCompletion(true);
		winjob.waitForCompletion(true);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("playedpath", playedpath);
		resultMap.put("losepath", losepath);
		resultMap.put("winpath", winpath);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> winratechartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception {
		String win = params.get("winpath").toString();
		String lose = params.get("losepath").toString();
		String played = params.get("playedpath").toString();
		String teamname = req.getParameter("teamname");
		
		URI winuri = URI.create(win + "/part-r-00000");
		URI loseuri = URI.create(lose + "/part-r-00000");
		URI playeduri = URI.create(played + "/part-r-00000");
		
		Path winpath = new Path(winuri);
		FileSystem winfile = FileSystem.get(winuri, conf);
		FSDataInputStream winfsis = winfile.open(winpath);
		byte[] winbuffer = new byte[5000];
		int winbyteRead = 0;
		String winresult = "";
		while((winbyteRead = winfsis.read(winbuffer)) > 0) { 
			winresult = new String(winbuffer, 0, winbyteRead);
		}
		
		Path losepath = new Path(loseuri);
		FileSystem losefile = FileSystem.get(loseuri, conf);
		FSDataInputStream losefsis = losefile.open(losepath);
		byte[] losebuffer = new byte[5000];
		int losebyteRead = 0;
		String loseresult = "";
		while((losebyteRead = losefsis.read(losebuffer)) > 0) { 
			loseresult = new String(losebuffer, 0, losebyteRead);
		}
		
		Path playedpath = new Path(playeduri);
		FileSystem playedfile = FileSystem.get(playeduri, conf);
		FSDataInputStream playedfsis = playedfile.open(playedpath);
		byte[] playedbuffer = new byte[5000];
		int playedbyteRead = 0;
		String playedresult = "";
		while((playedbyteRead = playedfsis.read(playedbuffer)) > 0) { 
			playedresult = new String(playedbuffer, 0, playedbyteRead);
		}
		
		String[] winrows = winresult.split("\n");
		List<HashMap<String, Object>> winlist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < winrows.length; j++) {
			String winrow = winrows[j];
			String[] wincols = winrow.split("\t");
			HashMap<String, Object> winmap = new HashMap<String, Object>();
			for(int c = 0; c < wincols.length; c++) {
				winmap.put(c + "", wincols[c]);
			}
			winlist.add(winmap);
		}
		
		String[] loserows = loseresult.split("\n");
		List<HashMap<String, Object>> loselist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < loserows.length; j++) {
			String loserow = loserows[j];
			String[] losecols = loserow.split("\t");
			HashMap<String, Object> losemap = new HashMap<String, Object>();
			for(int c = 0; c < losecols.length; c++) {
				losemap.put(c + "", losecols[c]);
			}
			loselist.add(losemap);
		}
		
		String[] playedrows = playedresult.split("\n");
		List<HashMap<String, Object>> playedlist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < playedrows.length; j++) {
			String playedrow = playedrows[j];
			String[] playedcols = playedrow.split("\t");
			HashMap<String, Object> playedmap = new HashMap<String, Object>();
			for(int c = 0; c < playedcols.length; c++) {
				playedmap.put(c + "", playedcols[c]);
			}
			playedlist.add(playedmap);
		}
		ArrayList<Object> datalist = new ArrayList<>();
		for (int z = 0; z < playedlist.size(); z++) {
			if(playedlist.get(z).get("0").equals(teamname)) {
				datalist.add(playedlist.get(z).get("1"));
			}
		}
		for (int c = 0; c < winlist.size(); c++) {
			if(winlist.get(c).get("0").equals(teamname)) {
				datalist.add(winlist.get(c).get("1"));
			}
		}
		for (int x = 0; x < loselist.size(); x++) {
			if(loselist.get(x).get("0").equals(teamname)) {
				datalist.add(loselist.get(x).get("1"));
			}
		}
		
		double winrate = (Double.parseDouble((String) datalist.get(1)) / Double.parseDouble((String) datalist.get(0)));
		double loserate = (Double.parseDouble((String) datalist.get(2)) / Double.parseDouble((String) datalist.get(0)));
		double drawrate = ((Double.parseDouble((String) datalist.get(0)) - Double.parseDouble((String) datalist.get(1)) - Double.parseDouble((String) datalist.get(2))) / Double.parseDouble((String) datalist.get(0)));
		String realwinrate = (String.format("%.2f", winrate));
		String realloserate = (String.format("%.2f", loserate));
		String realdrawrate = (String.format("%.2f", drawrate));
		
		List<HashMap<String, Object>> winrateresult = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> loserateresult = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> drawrateresult = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> winresultdata = new HashMap<String, Object>();
		HashMap<String, Object> loseresultdata = new HashMap<String, Object>();
		HashMap<String, Object> drawresultdata = new HashMap<String, Object>();
		winresultdata.put("winrate", realwinrate);
		loseresultdata.put("loserate", realloserate);
		drawresultdata.put("drawrate", realdrawrate);
		winrateresult.add(winresultdata);
		loserateresult.add(loseresultdata);
		drawrateresult.add(drawresultdata);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("winresult", winlist);
		resultMap.put("loseresult", loselist);
		resultMap.put("playedresult", playedlist);
		resultMap.put("winrateresult", winrateresult);
		resultMap.put("loserateresult", loserateresult);
		resultMap.put("drawrateresult", drawrateresult);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> goallosegoalrate(HttpServletRequest req) throws Exception {
		String league = req.getParameter("league");
		String inputpath = "/input/csv/" + league + "/" + league + ".csv";
		
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String realDate = date.format(today);
		
		Job goaljob = Job.getInstance(conf, "goal");
		Job losegoaljob = Job.getInstance(conf, "losegoal");
		
		
		URI goalinputUri = URI.create(inputpath); 
		URI goaloutputUri = URI.create("/result/" + league + "/goal/" + realDate);
		String goalpath = "/result/" + league + "/goal/" + realDate;
		
		URI losegoalinputUri = URI.create(inputpath); 
		URI losegoaloutputUri = URI.create("/result/" + league + "/losegoal/" + realDate);
		String losegoalpath = "/result/" + league + "/losegoal/" + realDate;
		
		
		FileInputFormat.addInputPath(goaljob, new Path(goalinputUri)); 		   
		FileOutputFormat.setOutputPath(goaljob, new Path(goaloutputUri));
		
		FileInputFormat.addInputPath(losegoaljob, new Path(losegoalinputUri)); 		   
		FileOutputFormat.setOutputPath(losegoaljob, new Path(losegoaloutputUri));
		
		
		goaljob.setInputFormatClass(TextInputFormat.class);	
		goaljob.setOutputFormatClass(TextOutputFormat.class); 
		
		losegoaljob.setInputFormatClass(TextInputFormat.class);	
		losegoaljob.setOutputFormatClass(TextOutputFormat.class);
		
		
		goaljob.setOutputKeyClass(Text.class);
		goaljob.setOutputValueClass(IntWritable.class);
		
		
		losegoaljob.setOutputKeyClass(Text.class);
		losegoaljob.setOutputValueClass(IntWritable.class);
		
		
		goaljob.setJarByClass(this.getClass()); 
		goaljob.setMapperClass(goalmapper.class);
		goaljob.setReducerClass(goalreduce.class);
		
		losegoaljob.setJarByClass(this.getClass()); 
		losegoaljob.setMapperClass(losegoalmapper.class);
		losegoaljob.setReducerClass(losegoalreduce.class);
		
		
		goaljob.waitForCompletion(true);
		losegoaljob.waitForCompletion(true);
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("goalpath", goalpath);
		resultMap.put("losegoalpath", losegoalpath);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> goallosegoalchartdata(HttpServletRequest req, HashMap<String, Object> params)
			throws Exception {
		String goal = params.get("goalpath").toString();
		String losegoal = params.get("losegoalpath").toString();
		String teamname = req.getParameter("teamname");
		
		URI goaluri = URI.create(goal + "/part-r-00000");
		URI losegoaluri = URI.create(losegoal + "/part-r-00000");
		
		Path goalpath = new Path(goaluri);
		FileSystem goalfile = FileSystem.get(goaluri, conf);
		FSDataInputStream goalfsis = goalfile.open(goalpath);
		byte[] goalbuffer = new byte[5000];
		int goalbyteRead = 0;
		String goalresult = "";
		while((goalbyteRead = goalfsis.read(goalbuffer)) > 0) { 
			goalresult = new String(goalbuffer, 0, goalbyteRead);
		}
		
		Path losegoalpath = new Path(losegoaluri);
		FileSystem losegoalfile = FileSystem.get(losegoaluri, conf);
		FSDataInputStream losegoalfsis = losegoalfile.open(losegoalpath);
		byte[] losegoalbuffer = new byte[5000];
		int losegoalbyteRead = 0;
		String losegoalresult = "";
		while((losegoalbyteRead = losegoalfsis.read(losegoalbuffer)) > 0) { 
			losegoalresult = new String(losegoalbuffer, 0, losegoalbyteRead);
		}
		
		
		String[] goalrows = goalresult.split("\n");
		List<HashMap<String, Object>> goallist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < goalrows.length; j++) {
			String goalrow = goalrows[j];
			String[] goalcols = goalrow.split("\t");
			HashMap<String, Object> goalmap = new HashMap<String, Object>();
			for(int c = 0; c < goalcols.length; c++) {
				goalmap.put(c + "", goalcols[c]);
			}
			goallist.add(goalmap);
		}
		
		String[] losegoalrows = losegoalresult.split("\n");
		List<HashMap<String, Object>> losegoallist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < losegoalrows.length; j++) {
			String losegoalrow = losegoalrows[j];
			String[] losegoalcols = losegoalrow.split("\t");
			HashMap<String, Object> losegoalmap = new HashMap<String, Object>();
			for(int c = 0; c < losegoalcols.length; c++) {
				losegoalmap.put(c + "", losegoalcols[c]);
			}
			losegoallist.add(losegoalmap);
		}
		
		
		ArrayList<Object> datalist = new ArrayList<>();
		
		for (int z = 0; z < goallist.size(); z++) {
			if(goallist.get(z).get("0").equals(teamname)) {
				datalist.add(goallist.get(z).get("1"));
			}
		}
		for (int x = 0; x < losegoallist.size(); x++) {
			if(losegoallist.get(x).get("0").equals(teamname)) {
				datalist.add(losegoallist.get(x).get("1"));
			}
		}
		
		int goalcount = Integer.parseInt((String) datalist.get(0));
		int losegoalcount = Integer.parseInt((String) datalist.get(1));
		int gmlgcount = (Integer.parseInt((String) datalist.get(0)) - Integer.parseInt((String) datalist.get(1)));
		
		
		List<HashMap<String, Object>> goalcountresult = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> losegoalcountresult = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> gmlgcountresult = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> goalresultdata = new HashMap<String, Object>();
		HashMap<String, Object> losegoalresultdata = new HashMap<String, Object>();
		HashMap<String, Object> gmlgresultdata = new HashMap<String, Object>();
		goalresultdata.put("goalcount", goalcount);
		losegoalresultdata.put("losegoalcount", losegoalcount);
		gmlgresultdata.put("gmlgcount", gmlgcount);
		goalcountresult.add(goalresultdata);
		losegoalcountresult.add(losegoalresultdata);
		gmlgcountresult.add(gmlgresultdata);
		
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("goalcountresult", goalcountresult);
		resultMap.put("losegoalcountresult", losegoalcountresult);
		resultMap.put("gmlgcountresult", gmlgcountresult);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> playedpergoal(HttpServletRequest req) throws Exception {
		String league = req.getParameter("league");
		String inputpath = "/input/csv/" + league + "/" + league + ".csv";
		
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String realDate = date.format(today);
		
		Job playedjob = Job.getInstance(conf, "played");
		Job goaljob = Job.getInstance(conf, "goal");
		Job losegoaljob = Job.getInstance(conf, "losegoal");
		
		
		URI playedinputUri = URI.create(inputpath); 
		URI playedoutputUri = URI.create("/result/" + league + "/played/" + realDate);
		String playedpath = "/result/" + league + "/played/" + realDate;
		
		URI goalinputUri = URI.create(inputpath); 
		URI goaloutputUri = URI.create("/result/" + league + "/goal/" + realDate);
		String goalpath = "/result/" + league + "/goal/" + realDate;
		
		URI losegoalinputUri = URI.create(inputpath); 
		URI losegoaloutputUri = URI.create("/result/" + league + "/losegoal/" + realDate);
		String losegoalpath = "/result/" + league + "/losegoal/" + realDate;
		
		FileInputFormat.addInputPath(playedjob, new Path(playedinputUri)); 		   
		FileOutputFormat.setOutputPath(playedjob, new Path(playedoutputUri));
		
		FileInputFormat.addInputPath(goaljob, new Path(goalinputUri)); 		   
		FileOutputFormat.setOutputPath(goaljob, new Path(goaloutputUri));
		
		FileInputFormat.addInputPath(losegoaljob, new Path(losegoalinputUri)); 		   
		FileOutputFormat.setOutputPath(losegoaljob, new Path(losegoaloutputUri));
		
		playedjob.setInputFormatClass(TextInputFormat.class);	
		playedjob.setOutputFormatClass(TextOutputFormat.class); 
		
		goaljob.setInputFormatClass(TextInputFormat.class);	
		goaljob.setOutputFormatClass(TextOutputFormat.class);
		
		losegoaljob.setInputFormatClass(TextInputFormat.class);	
		losegoaljob.setOutputFormatClass(TextOutputFormat.class);
		
		playedjob.setOutputKeyClass(Text.class);
		playedjob.setOutputValueClass(IntWritable.class);
		
		
		goaljob.setOutputKeyClass(Text.class);
		goaljob.setOutputValueClass(IntWritable.class);
		
		losegoaljob.setOutputKeyClass(Text.class);
		losegoaljob.setOutputValueClass(IntWritable.class);
		
		playedjob.setJarByClass(this.getClass()); 
		playedjob.setMapperClass(playedmapper.class);
		playedjob.setReducerClass(playedreduce.class);
		
		goaljob.setJarByClass(this.getClass()); 
		goaljob.setMapperClass(goalmapper.class);
		goaljob.setReducerClass(goalreduce.class);
		
		losegoaljob.setJarByClass(this.getClass()); 
		losegoaljob.setMapperClass(losegoalmapper.class);
		losegoaljob.setReducerClass(losegoalreduce.class);
		
		playedjob.waitForCompletion(true);
		goaljob.waitForCompletion(true);
		losegoaljob.waitForCompletion(true);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("playedpath", playedpath);
		resultMap.put("goalpath", goalpath);
		resultMap.put("losegoalpath", losegoalpath);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> playedpergoalchartdata(HttpServletRequest req, HashMap<String, Object> params)
			throws Exception {
		String played = params.get("playedpath").toString();
		String goal = params.get("goalpath").toString();
		String losegoal = params.get("losegoalpath").toString();
		String teamname = req.getParameter("teamname");
		
		URI playeduri = URI.create(played + "/part-r-00000");
		URI goaluri = URI.create(goal + "/part-r-00000");
		URI losegoaluri = URI.create(losegoal + "/part-r-00000");
		
		Path playedpath = new Path(playeduri);
		FileSystem playedfile = FileSystem.get(playeduri, conf);
		FSDataInputStream playedfsis = playedfile.open(playedpath);
		byte[] playedbuffer = new byte[5000];
		int playedbyteRead = 0;
		String playedresult = "";
		while((playedbyteRead = playedfsis.read(playedbuffer)) > 0) { 
			playedresult = new String(playedbuffer, 0, playedbyteRead);
		}
		
		Path goalpath = new Path(goaluri);
		FileSystem goalfile = FileSystem.get(goaluri, conf);
		FSDataInputStream goalfsis = goalfile.open(goalpath);
		byte[] goalbuffer = new byte[5000];
		int goalbyteRead = 0;
		String goalresult = "";
		while((goalbyteRead = goalfsis.read(goalbuffer)) > 0) { 
			goalresult = new String(goalbuffer, 0, goalbyteRead);
		}
		
		Path losegoalpath = new Path(losegoaluri);
		FileSystem losegoalfile = FileSystem.get(losegoaluri, conf);
		FSDataInputStream losegoalfsis = losegoalfile.open(losegoalpath);
		byte[] losegoalbuffer = new byte[5000];
		int losegoalbyteRead = 0;
		String losegoalresult = "";
		while((losegoalbyteRead = losegoalfsis.read(losegoalbuffer)) > 0) { 
			losegoalresult = new String(losegoalbuffer, 0, losegoalbyteRead);
		}
		
		String[] playedrows = playedresult.split("\n");
		List<HashMap<String, Object>> playedlist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < playedrows.length; j++) {
			String playedrow = playedrows[j];
			String[] playedcols = playedrow.split("\t");
			HashMap<String, Object> playedmap = new HashMap<String, Object>();
			for(int c = 0; c < playedcols.length; c++) {
				playedmap.put(c + "", playedcols[c]);
			}
			playedlist.add(playedmap);
		}
		
		String[] goalrows = goalresult.split("\n");
		List<HashMap<String, Object>> goallist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < goalrows.length; j++) {
			String goalrow = goalrows[j];
			String[] goalcols = goalrow.split("\t");
			HashMap<String, Object> goalmap = new HashMap<String, Object>();
			for(int c = 0; c < goalcols.length; c++) {
				goalmap.put(c + "", goalcols[c]);
			}
			goallist.add(goalmap);
		}
		
		String[] losegoalrows = losegoalresult.split("\n");
		List<HashMap<String, Object>> losegoallist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < losegoalrows.length; j++) {
			String losegoalrow = losegoalrows[j];
			String[] losegoalcols = losegoalrow.split("\t");
			HashMap<String, Object> losegoalmap = new HashMap<String, Object>();
			for(int c = 0; c < losegoalcols.length; c++) {
				losegoalmap.put(c + "", losegoalcols[c]);
			}
			losegoallist.add(losegoalmap);
		}
		ArrayList<Object> datalist = new ArrayList<>();
		for (int z = 0; z < playedlist.size(); z++) {
			if(playedlist.get(z).get("0").equals(teamname)) {
				datalist.add(playedlist.get(z).get("1"));
			}
		}
		for (int c = 0; c < goallist.size(); c++) {
			if(goallist.get(c).get("0").equals(teamname)) {
				datalist.add(goallist.get(c).get("1"));
			}
		}
		for (int x = 0; x < losegoallist.size(); x++) {
			if(losegoallist.get(x).get("0").equals(teamname)) {
				datalist.add(losegoallist.get(x).get("1"));
			}
		}
		
		double goalperplayedrate1 = (Double.parseDouble((String) datalist.get(1)) / Double.parseDouble((String) datalist.get(0)));
		double losegoalperplayedrate1 = (Double.parseDouble((String) datalist.get(2)) / Double.parseDouble((String) datalist.get(0)));
		String goalperplayedrate = (String.format("%.2f", goalperplayedrate1));
		String losegoalperplayedrate = (String.format("%.2f", losegoalperplayedrate1));
		List<HashMap<String, Object>> goalperplayedresult = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> losegoalperplayedresult = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> goalperplayedresultresultdata = new HashMap<String, Object>();
		HashMap<String, Object> losegoalperplayedresultdata = new HashMap<String, Object>();
		goalperplayedresultresultdata.put("goalperplayedrate", goalperplayedrate);
		losegoalperplayedresultdata.put("losegoalperplayedrate", losegoalperplayedrate);
		goalperplayedresult.add(goalperplayedresultresultdata);
		losegoalperplayedresult.add(losegoalperplayedresultdata);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("countlist", datalist);
		resultMap.put("goalperplayedresult", goalperplayedresult);
		resultMap.put("losegoalperplayedresult", losegoalperplayedresult);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> teamrank(HttpServletRequest req) throws Exception {
		String league = req.getParameter("league");
		String inputpath = "/input/csv/" + league + "/" + league + ".csv";
		
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String realDate = date.format(today);
		
		Job teamrankjob = Job.getInstance(conf, "teamrank");
		
		
		URI teamrankinputUri = URI.create(inputpath); 
		URI teamrankoutputUri = URI.create("/result/" + league + "/teamrank/" + realDate);
		String teamrankpath = "/result/" + league + "/teamrank/" + realDate;
		
		
		FileInputFormat.addInputPath(teamrankjob, new Path(teamrankinputUri)); 		   
		FileOutputFormat.setOutputPath(teamrankjob, new Path(teamrankoutputUri));
		
		
		teamrankjob.setInputFormatClass(TextInputFormat.class);	
		teamrankjob.setOutputFormatClass(TextOutputFormat.class); 
		
		
		teamrankjob.setOutputKeyClass(Text.class);
		teamrankjob.setOutputValueClass(IntWritable.class);
		
		teamrankjob.setJarByClass(this.getClass()); 
		teamrankjob.setMapperClass(rankmapper.class);
		teamrankjob.setReducerClass(rankreduce.class);
		
		teamrankjob.waitForCompletion(true);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("teamrankpath", teamrankpath);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> teamrankchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception {
		String teamrank = params.get("teamrankpath").toString();
		String teamname = req.getParameter("teamname");
		
		URI teamrankuri = URI.create(teamrank + "/part-r-00000");
		
		Path teamrankpath = new Path(teamrankuri);
		FileSystem teamrankfile = FileSystem.get(teamrankuri, conf);
		FSDataInputStream teamrankfsis = teamrankfile.open(teamrankpath);
		byte[] teamrankbuffer = new byte[5000];
		int teamrankbyteRead = 0;
		String teamrankresult = "";
		while((teamrankbyteRead = teamrankfsis.read(teamrankbuffer)) > 0) { 
			teamrankresult = new String(teamrankbuffer, 0, teamrankbyteRead);
		}
		
		
		
		String[] teamrankrows = teamrankresult.split("\n");
		List<HashMap<String, Object>> teamranklist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < teamrankrows.length; j++) {
			String teamrankrow = teamrankrows[j];
			String[] teamrankcols = teamrankrow.split("\t");
			HashMap<String, Object> teamrankmap = new HashMap<String, Object>();
			for(int c = 0; c < teamrankcols.length; c++) {
				teamrankmap.put(c + "", teamrankcols[c]);
			}
			teamranklist.add(teamrankmap);
		}
		System.out.println(teamranklist);
		
		
		ArrayList<Object> datalist = new ArrayList<>();
		for (int z = 0; z < teamranklist.size(); z++) {
			if(teamranklist.get(z).get("0").toString().contains(teamname)) {
				String rank = teamranklist.get(z).get("0").toString().replace(teamname, "");
				teamranklist.get(z).put("0", rank);
				datalist.add(teamranklist.get(z));
			}
		}
		
		HashMap<String, Object> first = new HashMap<String, Object>();
		HashMap<String, Object> second = new HashMap<String, Object>();
		HashMap<String, Object> stay = new HashMap<String, Object>();
		HashMap<String, Object> degrade = new HashMap<String, Object>();
		int degradecount = 0;
		int staycount = 0;
		ArrayList asd = new ArrayList<>();
		for (int i = 0; i < datalist.size(); i ++) {
			HashMap<Object, Object> map = (HashMap<Object, Object>) datalist.get(i);
			if(map.get("0").equals("1")) {
				if(!map.get("1").toString().equals("")) {
					first.put("first", Integer.parseInt((String) map.get("1")));
				}else{
					first.put("first", 0);
				}
			}else if (map.get("0").equals("2")) {
				if(!map.get("1").toString().equals("")) {
					second.put("second", Integer.parseInt((String) map.get("1")));
				}else{
					second.put("second", 0);
				}
			}else if (map.get("0").equals("18") || map.get("0").equals("19") || map.get("0").equals("20")) {
				if(!map.get("1").toString().equals("")) {
					degradecount += Integer.parseInt((String) map.get("1"));
					degrade.put("degrade", degradecount);
				}else{
					degrade.put("degrade", 0);
				}
			}else {
				staycount += Integer.parseInt((String) map.get("1"));
				stay.put("stay", staycount);
				
			}
		}
		

		resultMap = new HashMap<String, Object>();
		resultMap.put("first", first);
		resultMap.put("second", second);
		resultMap.put("stay", stay);
		resultMap.put("degrade", degrade);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> topteamrank(HttpServletRequest req) throws Exception {
		String league = req.getParameter("league");
		String inputpath = "/input/csv/" + league + "/" + league + ".csv";
		
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_hhmmss");
		String realDate = date.format(today);
		
		Job topteamrankjob = Job.getInstance(conf, "topteamrank");
		
		
		URI topteamrankinputUri = URI.create(inputpath); 
		URI topteamrankoutputUri = URI.create("/result/" + league + "/topteamrank/" + realDate);
		String topteamrankpath = "/result/" + league + "/topteamrank/" + realDate;
		
		
		FileInputFormat.addInputPath(topteamrankjob, new Path(topteamrankinputUri)); 		   
		FileOutputFormat.setOutputPath(topteamrankjob, new Path(topteamrankoutputUri));
		
		
		topteamrankjob.setInputFormatClass(TextInputFormat.class);	
		topteamrankjob.setOutputFormatClass(TextOutputFormat.class); 
		
		
		topteamrankjob.setOutputKeyClass(Text.class);
		topteamrankjob.setOutputValueClass(IntWritable.class);
		
		topteamrankjob.setJarByClass(this.getClass()); 
		topteamrankjob.setMapperClass(toprankmapper.class);
		topteamrankjob.setReducerClass(toprankreduce.class);
		
		topteamrankjob.waitForCompletion(true);
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("topteamrankpath", topteamrankpath);
		return resultMap;
	}

	@Override
	public HashMap<String, Object> topteamrankchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception {
		String topteamrank = params.get("topteamrankpath").toString();
		String teamname = req.getParameter("teamname");
		
		URI topteamrankuri = URI.create(topteamrank + "/part-r-00000");
		
		Path topteamrankpath = new Path(topteamrankuri);
		FileSystem topteamrankfile = FileSystem.get(topteamrankuri, conf);
		FSDataInputStream topteamrankfsis = topteamrankfile.open(topteamrankpath);
		byte[] topteamrankbuffer = new byte[5000];
		int topteamrankbyteRead = 0;
		String topteamrankresult = "";
		while((topteamrankbyteRead = topteamrankfsis.read(topteamrankbuffer)) > 0) { 
			topteamrankresult = new String(topteamrankbuffer, 0, topteamrankbyteRead);
		}
		
		
		
		String[] topteamrankrows = topteamrankresult.split("\n");
		List<HashMap<String, Object>> topteamranklist = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < topteamrankrows.length; j++) {
			String topteamrankrow = topteamrankrows[j];
			String[] topteamrankcols = topteamrankrow.split("\t");
			HashMap<String, Object> topteamrankmap = new HashMap<String, Object>();
			for(int c = 0; c < topteamrankcols.length; c++) {
				topteamrankmap.put(c + "", topteamrankcols[c]);
			}
			topteamranklist.add(topteamrankmap);
		}
		customSort(topteamranklist, "1");
		
		String result = "";
	      
	      List<HashMap<String, Object>> mapList = (List<HashMap<String, Object>>) topteamranklist;
	      
	      JSONObject obj = new JSONObject();
	      
	      
	      net.sf.json.JSONArray jArray = new net.sf.json.JSONArray();
	      for(int i=0; i<4; i++) {
	         JSONObject sObject = new JSONObject();
	         
	         sObject.put("category", mapList.get(i).get("0"));
	         sObject.put("column-1", Integer.parseInt((String) mapList.get(i).get("1")));
	         
	         jArray.add(sObject);
	      }
	      obj.put("dataProvider", jArray);
	      
	      result = obj.get("dataProvider").toString();
	      System.out.println("----------------------------");
	      System.out.println(result);
		
		
		
		resultMap = new HashMap<String, Object>();
		resultMap.put("data", result);
		return resultMap;
	}
	public static List<HashMap<String, Object>> customSort(List<HashMap<String, Object>> list, String key) {
		int first, second;
		HashMap<String, Object> first_map, second_map;
		for(int i = 0; i < list.size() -1; i++){
            for(int j = 0; j < ((list.size() - 1) - i); j++){
                if(Integer.parseInt((String) list.get(j).get(key)) < Integer.parseInt((String) list.get(j+1).get(key))){
                	first = (j + 1);
                	second = (first + 1);
                	first_map = list.get(first);
                	second_map = list.get(j);
                	list.add(j, first_map);
                	list.remove(first);
                	list.add(first, second_map);
                	list.remove(second);
                }
            }
        }
		return list;
	}
	
	
}
