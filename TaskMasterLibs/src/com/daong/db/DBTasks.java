package com.daong.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class DBTasks extends DBCon {

	/**
	 * タスクを追加
	 *
	 * @param teamNo
	 * @param userNo
	 * @param startDate
	 * @param deadline
	 * @param details
	 * @return
	 */
	public boolean addTask(int teamNo, int userNo, int startDate, int deadline, String details) {
		String sql[] = {"insert into task_tbl(team_no, user_no, task_start, task_deadline, task_details) values(?, ?, ?, ?, ?)",
				"insert into task_updates_tbl(task_no, task_updated, current_status) values(?, ?, 0)"};

		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				PreparedStatement stmt1 = con.prepareStatement(sql[0]);
				PreparedStatement stmt2 = con.prepareStatement(sql[1])) {

			Calendar cal = Calendar.getInstance();
			int updated = (cal.get(Calendar.YEAR)  * 10000) +  ((cal.get(Calendar.MONTH) + 1) * 100) + cal.get(Calendar.DATE);

			stmt1.setInt(1, teamNo);
			stmt1.setInt(2, userNo);
			stmt1.setInt(3, startDate);
			stmt1.setInt(4, deadline);
			stmt1.setString(5, details);
			stmt1.executeUpdate();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select task_no from task_tbl where team_no = " + teamNo + " and user_no = " +
					userNo + " and task_start = " + startDate + " and task_deadline = " + deadline + " and task_details = '"+ details + "'");

			if (rs.last()) {
				int taskNo = rs.getInt("task_no");
				stmt2.setInt(1, taskNo);
				stmt2.setInt(2, updated);
				stmt2.executeUpdate();
				rs.close();
				stmt.close();
			} else {
				rs.close();
				stmt.close();
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 *タスクにコメントを追加。
	 *
	 * @param taskNo
	 * @param userNo
	 * @param comment
	 * @return
	 */
	public boolean addComment(int taskNo, int userNo, String comment) {
		String sql = "insert into task_comments_tbl(task_no, user_no, time, comment) values(?, ?, ?, ?)";
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = con.prepareStatement(sql)) {

			Calendar cal = Calendar.getInstance();
			stmt.setInt(1, taskNo);
			stmt.setInt(2, userNo);
			stmt.setLong(3, cal.getTimeInMillis());
			stmt.setString(4, comment);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * タスクテーブルのデータを読み込む
	 * @param teamNo
	 * @return
	 */
	public List<HashMap<String, String>> loadTasks(int teamNo) {
		String sql = "select * from task_tbl where team_no = "+ teamNo;
		List<HashMap<String, String>> tasks = null;
		try {
			tasks = getArr(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tasks;
	}

	/**
	 * タスク詳細を読み込む
	 * @param taskNo
	 * @return
	 */
	public List<HashMap<String, String>> loadTaskUpdates(int taskNo) {
		String sql = "select * from task_updates_tbl where task_no = " + taskNo;
		List<HashMap<String, String>> taskDet = null;
		try {
			taskDet = getArr(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return taskDet;

	}

	/**
	 * タスクのコメントを読み込む
	 * @param taskNo
	 * @return
	 */
	public List<HashMap<String, String>> loadComments(int taskNo) {
		String sql = "select * from task_comments_tbl where task_no = " + taskNo;
		List<HashMap<String, String>> taskComments = null;
		try {
			taskComments = getArr(sql);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return taskComments;
	}
	
	public void deleteTask(int taskNo) {
		String sql = "update task_updates_tbl set current_status = -1 where task_no = " + taskNo;
		try {
			updateDB(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
