package com.java.web.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.java.web.Dao.DaoInterface;
import com.java.web.util.HttpUtil;





@Controller
public class UserController {
	
	@Autowired
	DaoInterface di;
	
	@RequestMapping("/register")
	public ModelAndView register(HttpServletRequest req) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> checkid = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		String name = req.getParameter("name");
		String phone = req.getParameter("phone");
		checkid.put("id", id);
		checkid.put("sqlType", "user.checkid");
		checkid.put("sql", "selectOne");
		resultMap.put("registerdata", di.call(checkid));
		if (resultMap.get("registerdata") == null) {
			param.put("id", id);
			param.put("pw", pw);
			param.put("name", name);
			param.put("phone", phone);
			param.put("sqlType", "user.userInsert");
			param.put("sql", "insert");
			int status = (int) di.call(param);
			resultMap.put("status", status);
		}else if (resultMap.get("registerdata") != null){
			resultMap.put("status", 0);
		}
		return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/checkid")
	public ModelAndView checkid(HttpServletRequest req) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String id = req.getParameter("id");
		param.put("id", id);
		param.put("sqlType", "user.checkid");
		param.put("sql", "selectOne");
		resultMap.put("registerdata", di.call(param));
		if (resultMap.get("registerdata") == null) {
			resultMap.put("status", 0);
		}else if (resultMap.get("registerdata") != null){
			resultMap.put("status", 1);
		}
		return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");
		param.put("id", id);
	    param.put("pw", pw);
		param.put("sqlType", "user.userSelect");
     	param.put("sql", "selectOne");
     	HashMap<String, Object> status = (HashMap<String, Object>) di.call(param); 
     	if(status == null) {
     		resultMap.put("status", 0);
     	}else {
     		resultMap = status;
			resultMap.put("status", 1);
     	}
     	session.setAttribute("user", resultMap);
     	System.out.println(resultMap);
     	return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/logoutcomplete")
	public String logout(HttpSession session) {
		session.invalidate();
	    return "logout";
	}
	
	@RequestMapping("/sessioncheck")
	public void sessioncheck(HttpSession session, HttpServletResponse res) {
	HashMap<String, Object> result = new HashMap<String, Object>();
	HashMap<String, Object> user = (HashMap<String, Object>)session.getAttribute("user");
	
	if(user == null) {
		result.put("result", 0);
		HttpUtil.makeJsonWriter(res, result); 				
	}
	else if(user.get("status").toString().equals("1")) {
		String id = (String) user.get("id");
		result.put("result", 1);
		result.put("id", id);
		HttpUtil.makeJsonWriter(res, result); 
		System.out.println(result);
	}
	else {
		result.put("result", 0);
		HttpUtil.makeJsonWriter(res, result); 			
	}
	}
}
