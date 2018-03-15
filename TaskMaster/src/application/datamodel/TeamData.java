package application.datamodel;

import java.util.HashMap;
import java.util.List;

import com.daong.db.DBUsers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TeamData {
	private ObservableList<TeamMember> members;
	private DBUsers db;
	private int teamNumber;
	
	public TeamData(ObservableList<TeamMember> members, int teamNumber) {
		super();
		this.members = members;
		this.db = new DBUsers();
		this.teamNumber = teamNumber;
	}
	
	public TeamData(int teamNumber) {
		this.members = FXCollections.observableArrayList();
		this.db = new DBUsers();
		this.teamNumber = teamNumber;
	}
	
	public void loadTeam() {
		List<HashMap<String, String>> uList = db.getUsers();
		for (HashMap<String, String> hm: uList) {
			int teamNo = Integer.parseInt(hm.get("team_no"));
			if (teamNo == this.teamNumber) {
				members.add(new TeamMember(Integer.parseInt(hm.get("user_no")), hm.get("email"), hm.get("name"), Integer.parseInt(hm.get("member_type")), this.teamNumber));
			}
			
		}
	}

	public ObservableList<TeamMember> getMembers() {
		return members;
	}

	public void setMembers(ObservableList<TeamMember> members) {
		this.members = members;
	}

	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	public void addToTeam(String email) {
		DBUsers db = new DBUsers();
		db.removeFromTeam(email, -1);
		db.addToTeam(email, this.teamNumber);		
	}

	public void createTeam(String email) {
		DBUsers db = new DBUsers();
		int numberOfTeams = db.getNumberOfTeams();
		db.removeFromTeam(email, -1);
		db.addToTeam(email, numberOfTeams + 1);
	}
				
}
