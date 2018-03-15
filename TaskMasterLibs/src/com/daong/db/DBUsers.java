package com.daong.db;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import com.daong.cipher.Crypto;

//プレースホルダーメンバーとして　email = daniel1～15@daniel.com password = thisisapassword　ソルトとIVはメンバーを追加するたび自動的に作成
public final class DBUsers extends DBCon {

	/**
	 *メンバー追加する。プレーンテキストでPWを保存せず、ソルトを振って、暗号化してDBでBLOBとして保存する。
	 * @param email
	 * @param password
	 * @param name
	 * @param memberType
	 * @return boolean
	 *
	 */
	public boolean addMember(String email, String password, String name, int memberType ) {
		
		String salt = Crypto.randomString(8);
		String iv = Crypto.randomString(16);

		byte[] pwBytes = passBytes(password + salt, iv);

		String sql = "insert into login_tbl (email, password, salt, iv) values('" + email + "', ?, '" + salt + "', '" + iv + "')";
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = con.prepareStatement(sql)){

			Blob bl = con.createBlob();
			bl.setBytes(1, pwBytes, 0, pwBytes.length);

			stmt.setBlob(1, bl);
			stmt.executeUpdate();
			bl.free();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		sql = "select user_no from login_tbl where email = '" + email +"'";

		String userNo = null;
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				userNo = rs.getString("user_no");
			}

			sql = "insert into member_tbl(user_no, name, member_type) values(" + userNo + ", '" + name + "', " + memberType + ")";
			updateDB(sql);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * PWを暗号化しバイト配列を返す。
	 * @param password
	 * @return byte[]
	 */
	private byte[] passBytes(String password, String iv) {
		Crypto crypt = new Crypto(iv);
		byte[] bytes = crypt.encrypt(password);

		return bytes;
	}

	/**
	 * パスワード更新
	 * @param email
	 * @param password
	 * @return boolean
	 */
	public boolean updatePassword(String email, String password) {
		String salt = Crypto.randomString(8);
		String iv = Crypto.randomString(16);
		byte[] pwBytes = passBytes(password + salt, iv);

		String sql = "update login_tbl set password = ?, iv = '" + iv + "', salt = '" + salt + "' where email = '" + email + "'";
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				PreparedStatement stmt = con.prepareStatement(sql)){

			Blob bl = con.createBlob();
			bl.setBytes(1, pwBytes, 0, pwBytes.length);

			stmt.setBlob(1, bl);
			stmt.executeUpdate();
			bl.free();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * ユーザ認証
	 * @param email
	 * @param password
	 * @return int
	 */
	public int login(String email, String password) {
		String sql = "select user_no, password, salt, iv from login_tbl where email = '" + email + "'";
		int userNo = -1;
		Blob pwByteBlob = null;
		byte[] bytes = null;
		String salt = null;

		byte[] savedPW = null;
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
				
			if (rs.next()) {
				userNo = rs.getInt("user_no");
				pwByteBlob = rs.getBlob("password");
				salt = rs.getString("salt");
				String iv = rs.getString("iv");
				bytes = pwByteBlob.getBytes(1, (int)pwByteBlob.length());
				savedPW = passBytes(password + salt, iv);
				for (int i = 0; i < savedPW.length; i++) {
					if (bytes[i] == savedPW[i]){
						continue;
					} else {
						return -1;
					}
				}
				pwByteBlob.free();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return userNo;
	}

	/**
	 * メンバーをチームに追加
	 * @param email
	 * @param team
	 * @return boolean
	 */
	public boolean addToTeam(String email, int team) {
		String sql = "select user_no from login_tbl where email = '" + email + "'";
		try {
			List<HashMap<String,String>> list = getArr(sql);
			String userNo = list.get(0).get("user_no");
			sql = "insert into team_tbl(team_no, user_no) values(" + team + ", " + userNo + ")";
			updateDB(sql);

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}
	
	/**
	 * メンバーをチームから外す
	 * @param email
	 * @param team
	 * @return boolean
	 */
	public boolean removeFromTeam(String email, int team) {
		String sql = "select user_no from login_tbl where email = '" + email + "'";
		try {
			List<HashMap<String,String>> list = getArr(sql);
			String userNo = list.get(0).get("user_no");
			sql = "delete from team_tbl where user_no = " + userNo + " and team_no = " + team + "";
			updateDB(sql);

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	/**
	 * ユーザーデータをDBから取得ユーザー番号、メアド、名前、メンバー種別、チーム番号
	 *
	 * @return List<HashMap>
	 */
	public List<HashMap<String, String>> getUsers(){
		String sql = "select login_tbl.user_no, login_tbl.email, member_tbl.name, member_tbl.member_type, team_tbl.team_no from login_tbl, member_tbl, team_tbl where team_tbl.user_no = login_tbl.user_no and member_tbl.user_no = login_tbl.user_no";
		try {
			return super.getArr(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * チーム数を数える
	 * @return int
	 */
	public int getNumberOfTeams() {
		String sql = "select count(distinct team_no) from team_tbl";
			
		try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				return rs.getInt(1) - 1;		
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
}


