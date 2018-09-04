package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class winbean {
	String clubname;
	int win;
	
	public winbean(Text value) {
		try {
			String[] col = value.toString().split(",");
			
			setClubname(col[1] == null ? "" : col[1]);
			setWin(Integer.parseInt(col[3].equals("0")?"0" : col[3]));
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

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	@Override
	public String toString() {
		return "winbean [clubname=" + clubname + ", win=" + win + "]";
	}
	
}
