package com.java.web.bean;

import org.apache.hadoop.io.Text;

public class rankbean {
	String clubname;
	int rank;
	
	public rankbean(Text value) {
		try {
			String[] col = value.toString().split(",");
			setClubname(col[1] == null ? "" : col[1]);
			setRank(Integer.parseInt(col[0].equals("0")?"0" : col[0]));
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "rankbean [clubname=" + clubname + ", rank=" + rank + "]";
	}
	
}
