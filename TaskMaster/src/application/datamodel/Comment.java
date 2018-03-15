package application.datamodel;

import java.util.Calendar;

public class Comment {
	private String memberName;
	private Integer userNumber;
	private Long longTime;
	private String comment;
	private String time;
	
	
	public Comment(String memberName, int userNumber, long longTime, String comment) {
		this.memberName = memberName;
		this.longTime = longTime;
		this.comment = comment;
		this.userNumber = userNumber;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(longTime);
				
		this.time = cal.getTime().toString();
	}
	
	public Comment(Comment  comment) {
		this.memberName = comment.getMemberName();
		this.time = comment.getTime();
		this.comment = comment.getComment();
		this.userNumber = comment.getUserNumber();
		this.longTime = comment.getLongTime();
	}

	public Comment() {
		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj != null) {
			if (this.getClass() == obj.getClass()) {
				return (this.memberName + this.comment + this.time).equals(((Comment)obj).getMemberName() + ((Comment)obj).getComment() + ((Comment)obj).getTime());
			}
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		return this.memberName.hashCode() + this.comment.hashCode() + this.time.hashCode();
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	public Long getLongTime() {
		return longTime;
	}

	public void setLongTime(Long longTime) {
		this.longTime = longTime;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(longTime);
				
		this.time = cal.getTime().toString();
	}

	
	
	
	
		
}
