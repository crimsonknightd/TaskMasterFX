package application.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import com.daong.db.DBTasks;
import com.daong.db.DBUsers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskData {
	private ObservableList<Task> tasks;
	private int teamNo;
	private String email;
	private int userNumber;
	private DBUsers u = new DBUsers();
	
	public TaskData(String email) {
		tasks = FXCollections.observableArrayList();
		this.teamNo = findTeamNumber(email);
		this.email = email;
		this.userNumber = findUserNumber(email);
	}
	
	public void addTask(Task newTask) {
		DBTasks t = new DBTasks();
		t.addTask(teamNo, newTask.getUserNumber(), newTask.getStartDate(), newTask.getDeadline(), newTask.getDetails());
		this.loadTasks();
	}
	
	public void addComment(Task task, Comment comment) {
//		UnaryOperator uo = new UnaryOperator() {
//
//			@Override
//			public Object apply(Object t) {
//				if (((Task)t).equals(oldTask)) {
//					return task;
//				} 
//				return t;
//			}
//			
//		};の代わりになるラムダ式は t -> t.equals(oldTask) ? newTask : t
		
		Task oldTask = new Task(task);
		List<Comment> comments = task.getComments();
		comments.add(comment);
		//メモリー上commentsはtask内のコメントのListへのレファレンスですのでtask.setComments(comments)を使う必要ありません。
				
		if (tasks.contains(oldTask)) {
			tasks.replaceAll(t -> t.equals(oldTask) ? task : t);
			DBTasks t = new DBTasks();
			t.addComment(task.getTaskNumber(), comment.getUserNumber(), comment.getComment());
			
		}

	}

	public ObservableList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ObservableList<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void deleteTask(Task task) {
//		Predicate p = new Predicate() {
//			@Override
//			public boolean test(Object t) {
//				return ((Task)t).equals(task);
//			}
//		};のラムダ式は ｔ -> t.equals(task);
		int taskNumber = task.getTaskNumber();
		if (task.getUserNumber() == userNumber) {
			tasks.removeIf(t -> t.equals(task));
			DBTasks t = new DBTasks();
			t.deleteTask(taskNumber);
		}
	}
	
	public void loadTasks() {
		tasks.removeAll(tasks);
		DBTasks t = new DBTasks();
		List<HashMap<String,String>> ts = t.loadTasks(this.teamNo);
//		System.out.println(teamNo);
		int taskNo = -1;
		for (int i = 0; i < ts.size(); i++) {
			Task tempTask = new Task();
			taskNo = Integer.parseInt(ts.get(i).get("task_no"));
			List<HashMap<String,String>> taskUpdates= t.loadTaskUpdates(taskNo);
			List<HashMap<String,String>> taskComments = t.loadComments(taskNo);
			ObservableList<Comment> comments = FXCollections.observableArrayList();
			int team = findTeamNumber(Integer.parseInt(ts.get(i).get("user_no")));
			if (Integer.parseInt(taskUpdates.get(0).get("current_status")) > -1 && 
					this.teamNo == team) {
				comments = bundleComments(taskComments);
				
				tempTask.setComments(comments);
				tempTask.setDeadline(Integer.parseInt(ts.get(i).get("task_deadline")));
				tempTask.setMemberName(findMemberName(Integer.parseInt(ts.get(i).get("user_no"))));
				tempTask.setStartDate(Integer.parseInt(ts.get(i).get("task_start")));
				tempTask.setStatus(Integer.parseInt(taskUpdates.get(0).get("current_status")));
				tempTask.setTeamNumber(team);
				tempTask.setTaskNumber(taskNo);
				tempTask.setDetails(ts.get(i).get("task_details"));
				tempTask.setUserNumber(Integer.parseInt(ts.get(i).get("user_no")));
				tasks.add(tempTask);
			}
			
		}
		
	}

	private String findMemberName(int userNo) {
		DBUsers u = new DBUsers();
		String strUserNo = "" + userNo;
		List<HashMap<String, String>> us = u.getUsers();
		for (HashMap<String, String> hm: us) {
			if (hm.get("user_no").equals(strUserNo)) {
				return hm.get("name");
			}
		}
		return null;
	}

	private int findTeamNumber(String email) {
		List<HashMap<String, String>> us = u.getUsers();
		for (HashMap<String, String> hm: us) {
			if (hm.get("email").equals(email)) {
				return Integer.parseInt(hm.get("team_no"));
			}
		}
		return -1;
	}
	
	private int findTeamNumber(int userNo) {
		List<HashMap<String, String>> us = u.getUsers();
		String strUserNo = "" + userNo;
		for (HashMap<String, String> hm: us) {
			if (hm.get("user_no").equals(strUserNo)) {
				return Integer.parseInt(hm.get("team_no"));
			}
		}
		return 0;
	}
	
	private ObservableList<Comment> bundleComments(List<HashMap<String, String>> taskComments) {
		ObservableList<Comment> comments = FXCollections.observableArrayList();
		for (int i = 0; i < taskComments.size(); i++) {
			Comment c = new Comment();
			c.setComment(taskComments.get(i).get("comment"));
			int userNo = Integer.parseInt(taskComments.get(i).get("user_no"));
			String memberName = findMemberName(userNo);
			c.setMemberName(memberName);
			c.setLongTime(Long.parseLong(taskComments.get(i).get("time")));
			c.setUserNumber(userNo);
			comments.add(c);
		}
		return comments;

	}
	
	private int findUserNumber(String email) {
		List<HashMap<String, String>> us = u.getUsers();
		for (HashMap<String, String> hm: us) {
			if (hm.get("email").equals(email)) {
				return Integer.parseInt(hm.get("user_no"));
			}
		}
		return -1;
	}

	public int getTeamNumber() {
		return teamNo;
	}

	public int getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(int teamNo) {
		this.teamNo = teamNo;
	}
	
}
