package com.java.web.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "main";
	}
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(Locale locale, Model model) {
		return "main";
	}
	@RequestMapping(value = "/boardlist", method = RequestMethod.GET)
	public String boardlist(Locale locale, Model model) {
		return "boardlist";
	}
	@RequestMapping(value = "/boarddetail", method = RequestMethod.GET)
	public String boarddetail(Locale locale, Model model) {
		return "boarddetail";
	}
	@RequestMapping(value = "/boardwrite", method = RequestMethod.GET)
	public String boardwrite(Locale locale, Model model, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>)session.getAttribute("user");
		if (user == null) {
			return "boardlist";
		}else {
			return "boardwrite";
		}
	}
	@RequestMapping(value = "/england", method = RequestMethod.GET)
	public String england(Locale locale, Model model) {
		return "england";
	}
	@RequestMapping(value = "/france", method = RequestMethod.GET)
	public String france(Locale locale, Model model) {
		return "france";
	}
	@RequestMapping(value = "/italy", method = RequestMethod.GET)
	public String italy(Locale locale, Model model) {
		return "italy";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>)session.getAttribute("user");
		if (user == null) {
			return "login";
		}else {
			return "main";
		}
	}
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(Locale locale, Model model) {
		return "logout";
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Locale locale, Model model, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>)session.getAttribute("user");
		if (user == null) {
			return "register";
		}else {
			return "main";
		}
	}
	@RequestMapping(value = "/spain", method = RequestMethod.GET)
	public String spain(Locale locale, Model model) {
		return "spain";
	}
	@RequestMapping(value = "/boardupdate", method = RequestMethod.GET)
	public String boardupdate(HttpServletRequest req, Locale locale, Model model, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>)session.getAttribute("user");
		if (user == null) {
			return "boardlist";
		}else if(user != null) {
			String userid = (String) user.get("id");
			String id = req.getParameter("id");
			if (id.equals(userid)) {
				return "boardupdate";
			}else {
				return "boardlist";
			}
		}else {
			return "boardlist";
		}
		
	}
	@RequestMapping(value = "/introduce", method = RequestMethod.GET)
	public String introduce(Locale locale, Model model) {
		return "introduce";
	}
	
}
