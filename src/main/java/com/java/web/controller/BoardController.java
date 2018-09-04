package com.java.web.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.java.web.Dao.DaoInterface;
import com.java.web.util.HttpUtil;



@Controller
public class BoardController {

	
	@Autowired
	DaoInterface di;
	
	@RequestMapping("/boardwrite")
	public ModelAndView boardwrite(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		int userNo = (Integer) user.get("userNo");
		String title = req.getParameter("title");
	    String content = req.getParameter("content");
		String id = (String) user.get("id");
		if (id != null) {
			param.put("userNo", userNo);
			param.put("id", id);
			param.put("title", title);
			param.put("content", content);
			param.put("sqlType", "board.boardwrite");
			param.put("sql", "insert");
			int status = (int) di.call(param);
			resultMap.put("status", status);
		}else {
			resultMap.put("status", 0);
		}
	    return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/boardlist")
    public ModelAndView boardlist(HttpServletRequest req) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	int start = Integer.parseInt(req.getParameter("start"));
    	param.put("start", start);
    	param.put("sqlType", "board.boardlist");
    	param.put("sql", "selectList");
    	List list = (List) di.call(param);
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("list", list);
    	return HttpUtil.makeJsonView(resultMap);
    }
	@RequestMapping("/boardlistcount")
    public ModelAndView boardlistcount() {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	param.put("sqlType", "board.boardlistcount");
    	param.put("sql", "selectList");
    	List list = (List) di.call(param);
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("list", list);
    	return HttpUtil.makeJsonView(resultMap);
    }
	@RequestMapping("/boarddetail")
	public ModelAndView boarddetail(HttpServletRequest req) {
    	String boardNo = req.getParameter("boardNo");
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	param.put("boardNo", boardNo);
    	param.put("sqlType","board.boardOne");
    	param.put("sql","selectOne");
    	resultMap.put("boardData", di.call(param));
    	
    	return HttpUtil.makeJsonView(resultMap);
    }
	@RequestMapping("/viewcountup")
    public void viewcountup(HttpServletRequest req,HttpServletResponse res) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	String boardNo = req.getParameter("boardNo");
    	param.put("boardNo", boardNo);
    	param.put("sqlType","board.countup");
    	param.put("sql","update");
    	param.put("status", (int)di.call(param));
    	HttpUtil.makeJsonWriter(res,param);
    }
	@RequestMapping("/updatebtn")
	public ModelAndView register(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(user == null) {
			resultMap.put("result", 2);
		}else {
			String userid = (String) user.get("id");
			String id = req.getParameter("id");
			if (userid.equals(id)) {
				resultMap.put("result", 1);
			}else {
				resultMap.put("result", 0);
			}
		}
		System.out.println(resultMap);
		
		  return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/boarddelete")
	public ModelAndView boarddelete(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(user == null) {
			resultMap.put("result", 2);
		}else if (user != null) {
			String userid = (String) user.get("id");
			String id = req.getParameter("id");
			String boardNo = req.getParameter("boardNo");
			if (userid.equals(id)) {
				param.put("boardNo", boardNo);
	        	param.put("sqlType","board.boarddelete");
	        	param.put("sql","update");
				resultMap.put("result", 1);
				int status = (int)di.call(param);
	        	resultMap.put("status", status);
			}else {
				resultMap.put("result", 0);
			}
		}
		
		  return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/boardupdate")
	public ModelAndView boardupdate(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String boardNo = req.getParameter("boardNo");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		if(user != null) {
			String userid = req.getParameter("id");
			String id = (String)user.get("id");
			if (id.equals(userid)) {
				param.put("title", title);
				param.put("content", content);
				param.put("boardNo", boardNo);
	        	param.put("sqlType","board.boardupdate");
	        	param.put("sql","update");
				int status = (int)di.call(param);
	        	resultMap.put("status", status);
			}else {
				resultMap.put("status", 0);
			}
			
		}else {
			resultMap.put("status", 0);
		}
		return HttpUtil.makeJsonView(resultMap);
	}
	@RequestMapping("/search")
    public ModelAndView search(HttpServletRequest req) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	String keyword = req.getParameter("keyword");
    	String menu = req.getParameter("menu");
    	param.put("keyword", keyword);
    	param.put("menu", menu);
    	param.put("sqlType", "board.search");
    	param.put("sql", "selectList");
    	System.out.println(param);
    	List list = (List) di.call(param);
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("list", list);
    	return HttpUtil.makeJsonView(resultMap);
    }
	@RequestMapping("/commentlist")
    public ModelAndView commentlist(HttpServletRequest req) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	String boardNo = req.getParameter("boardNo");
    	int start = Integer.parseInt(req.getParameter("start"));
    	param.put("start", start);
    	param.put("boardNo", boardNo);
    	param.put("sqlType", "board.commentlist");
    	param.put("sql", "selectList");
    	List list = (List) di.call(param);
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("list", list);
    	return HttpUtil.makeJsonView(resultMap);
    }
	@RequestMapping("/commentlistcount")
    public ModelAndView commentlistcount(HttpServletRequest req) {
    	HashMap<String, Object> param = new HashMap<String, Object>();
    	String boardNo = req.getParameter("boardNo");
    	param.put("boardNo", boardNo);
    	param.put("sqlType", "board.commentlistcount");
    	param.put("sql", "selectList");
    	List list = (List) di.call(param);
    	HashMap<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("list", list);
    	return HttpUtil.makeJsonView(resultMap);
    }
	
	@RequestMapping("/commentwrite")
	public ModelAndView commentwrite(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String boardNo = req.getParameter("boardNo");
	    String content = req.getParameter("content");
		if (user != null) {
			int userNo = (Integer) user.get("userNo");
			String id = (String) user.get("id");
			param.put("userNo", userNo);
			param.put("id", id);
			param.put("boardNo", boardNo);
			param.put("content", content);
			param.put("sqlType", "board.commentwrite");
			param.put("sql", "insert");
			int status = (int) di.call(param);
			resultMap.put("status", status);
		}else{
			
			resultMap.put("status", 0);
		}
	    return HttpUtil.makeJsonView(resultMap);
	}
	
	@RequestMapping("/commentdelete")
	public ModelAndView commentdelete(HttpServletRequest req, HttpSession session) {
		HashMap<String, Object> user = (HashMap<String, Object>) session.getAttribute("user");
		HashMap<String, Object> param = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(user == null) {
			resultMap.put("result", 0);
		}else {
			String userid = (String) user.get("id");
			String id = req.getParameter("id");
			String commentNo = req.getParameter("commentNo");
			if (userid.equals(id)) {
				param.put("commentNo", commentNo);
				param.put("id", id);
	        	param.put("sqlType","board.commentdelete");
	        	param.put("sql","update");
				int status = (int)di.call(param);
				resultMap.put("result", 1);
			}else {
				resultMap.put("result", 2);
			}
		}
		
		  return HttpUtil.makeJsonView(resultMap);
	}
	
	
}
