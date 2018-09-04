package com.java.web.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java.web.util.HttpUtil;

@Controller
public class LeagueController {
	@RequestMapping("/teamlist")
	public ModelAndView teamlist(HttpServletRequest req) throws IOException, URISyntaxException {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String league = req.getParameter("league");
	    String year = req.getParameter("year");
	    Configuration configuration = new Configuration();
	    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop:9000"), configuration);
	    Path filePath = new Path("hdfs://hadoop:9000/input/csv/" + league + "/" + league + ".csv");
	    System.out.println(filePath);
	    FSDataInputStream fsDataInputStream = fs.open(filePath);
	    StringBuffer sb = new StringBuffer();
	    byte[] bt = new byte[50000];
	    int point = 0;
	    while((point = fsDataInputStream.read(bt)) != -1){
	    	sb.append(new String(bt, 0, point));
	    }	    
	    String[] rows = sb.toString().split("\n");
	    System.out.println(rows.toString());
	    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for(int j = 0; j < rows.length; j++) {
			String row = rows[j];
			String[] cols = row.split(",");
			HashMap<String, Object> map = new HashMap<String, Object>();
			for(int c = 0; c < cols.length; c++) {
				map.put(c + "", cols[c]);
			}
			list.add(map);
		}
		ArrayList teamname = new ArrayList<>();
		for(int k = 0; k < list.size(); k++) {
			if (!list.get(k).get("1").equals("") && !teamname.contains(list.get(k).get("1"))) {
				teamname.add(list.get(k).get("1"));
			}
		}
		System.out.println(teamname);
	    resultMap.put("teamlist", teamname);
	    return HttpUtil.makeJsonView(resultMap);
	}
	
	
}
