package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class playedbean {
	String clubname;
	int played;
	
	public playedbean(Text value) {
		try {
			String[] col = value.toString().split(",");
			setClubname(col[1] == null ? "" : col[1]);
			setPlayed(Integer.parseInt(col[2].equals("0")?"0" : col[2]));
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

	public int getPlayed() {
		return played;
	}

	public void setPlayed(int played) {
		this.played = played;
	}

	@Override
	public String toString() {
		return "playedbean [clubname=" + clubname + ", played=" + played + "]";
	}
	
}
