package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class losebean {
	String clubname;
	int lose;
	
	public losebean(Text value) {
		try {
			String[] col = value.toString().split(",");
			setClubname(col[1] == null ? "" : col[1]);
			setLose(Integer.parseInt(col[5].equals("0")?"0" : col[5]));
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

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	@Override
	public String toString() {
		return "losebean [clubname=" + clubname + ", lose=" + lose + "]";
	}
	
}
