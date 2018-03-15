package application.datamodel;

import javafx.collections.ObservableList;

public class Task {
	private Integer taskNumber;
	private Integer teamNumber;
	private String memberName;
	private Integer startDate;
	private Integer deadline;
	private String details;
	private Integer status;
	private ObservableList<Comment> comments;
	private Integer userNumber;
	
	public Task(int taskNumber, int teamNumber, String memberName, int startDate, int deadline, String details, int status,
			ObservableList<Comment> comments, int userNumber) {
		this.taskNumber = taskNumber;
		this.teamNumber = teamNumber;
		this.memberName = memberName;
		this.startDate = startDate;
		this.deadline = deadline;
		this.details = details;
		this.status = status;
		this.comments = comments;
		this.userNumber = userNumber;
	}
	
	public Task(Task task) {
		this.taskNumber = task.getTaskNumber();
		this.teamNumber = task.getTeamNumber();
		this.memberName = task.getMemberName();
		this.startDate = task.getStartDate();
		this.deadline = task.getDeadline();
		this.details = task.getDetails();
		this.status = task.getStatus();
		this.comments = task.getComments();
		this.userNumber = task.getUserNumber();
	}
	
	public Task() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj != null) {
			if (obj.getClass() == this.getClass()) {
				return (("" + this.taskNumber + this.details + this.memberName).equals("" + 
						((Task)obj).getTaskNumber() + ((Task)obj).getDetails() + ((Task)obj).getMemberName()));
			}
		} 
		
		return false;
	}

	@Override
	public int hashCode() {
		return this.details.hashCode() + this.memberName.hashCode() + this.taskNumber;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ObservableList<Comment> getComments() {
		return comments;
	}

	public void setComments(ObservableList<Comment> comments) {
		this.comments = comments;
	}

	public int getUserNumber() {
		return this.userNumber;
	}
	
	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}
}
