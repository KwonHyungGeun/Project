package com.java.web.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public interface CommonServiceInterface {
	
	public HashMap<String, Object> winrate(HttpServletRequest req) throws Exception;
	public HashMap<String, Object> winratechartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception;
	
	public HashMap<String, Object> goallosegoalrate(HttpServletRequest req) throws Exception;
	public HashMap<String, Object> goallosegoalchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception;
	
	public HashMap<String, Object> playedpergoal(HttpServletRequest req) throws Exception;
	public HashMap<String, Object> playedpergoalchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception;
	
	public HashMap<String, Object> teamrank(HttpServletRequest req) throws Exception;
	public HashMap<String, Object> teamrankchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception;
	
	public HashMap<String, Object> topteamrank(HttpServletRequest req) throws Exception;
	public HashMap<String, Object> topteamrankchartdata(HttpServletRequest req, HashMap<String, Object> params) throws Exception;
}
