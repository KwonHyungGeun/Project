package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class goalbean {
	String clubname;
	int goal;
	
	public goalbean(Text value) {
		try {
			String[] col = value.toString().split(",");
			setClubname(col[1] == null ? "" : col[1]);
			setGoal(Integer.parseInt(col[6].equals("0")?"0" : col[6]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getClubname() {
		return clubname;
	}

	public void setClubname(String clubname) {
		this.clubname = clubname;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		return "goalbean [clubname=" + clubname + ", goal=" + goal + "]";
	}
	
}
