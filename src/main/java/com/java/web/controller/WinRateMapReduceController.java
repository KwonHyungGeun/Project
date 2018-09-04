package com.java.web.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.java.web.service.CommonServiceInterface;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


@Controller
public class WinRateMapReduceController {
	
	@Resource(name="hdConf")
	Configuration conf;
	
	@Autowired
	CommonServiceInterface csi;
	
	HashMap<String, Object> resultMap;
	
	@RequestMapping(value="/analysisData", method = RequestMethod.POST)
	public void winrate(HttpServletRequest req, HttpServletResponse res) throws Exception {
		resultMap = new HashMap<String, Object>(); 
		resultMap.put("winrate", csi.winratechartdata(req, csi.winrate(req)));
		resultMap.put("goallosegoalrate", csi.goallosegoalchartdata(req, csi.goallosegoalrate(req)));
		resultMap.put("playedpergoal", csi.playedpergoalchartdata(req, csi.playedpergoal(req)));
		resultMap.put("teamrank", csi.teamrankchartdata(req, csi.teamrank(req)));
		resultMap.put("topteamrank", csi.topteamrankchartdata(req, csi.topteamrank(req)));
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/json;charset=utf-8");
		JSONObject json = JSONObject.fromObject(JSONSerializer.toJSON(resultMap));
		res.getWriter().write(json.toString());
	}
	
}
