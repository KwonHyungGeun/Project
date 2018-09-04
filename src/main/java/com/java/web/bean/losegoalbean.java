package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class losegoalbean {
	String clubname;
	int losegoal;
	
	public losegoalbean(Text value) {
		try {
			String[] col = value.toString().split(",");
			setClubname(col[1] == null ? "" : col[1]);
			setlosegoal(Integer.parseInt(col[7].equals("0")?"0" : col[7]));
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

	public int getlosegoal() {
		return losegoal;
	}

	public void setlosegoal(int losegoal) {
		this.losegoal = losegoal;
	}

	@Override
	public String toString() {
		return "losegoalbean [clubname=" + clubname + ", losegoal=" + losegoal + "]";
	}
	
}
