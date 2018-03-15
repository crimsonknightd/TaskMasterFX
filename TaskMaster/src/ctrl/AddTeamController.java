package ctrl;

import application.datamodel.TeamData;
import application.datamodel.TeamMember;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class AddTeamController {
	@FXML
	ComboBox<TeamMember> memberName;
	public void initMembers () {
		
		TeamData td = new TeamData(-1);
		td.loadTeam();
		ObservableList<TeamMember> members = td.getMembers();
		
		memberName.getItems().addAll(members);
		
	}
	
	public TeamMember processResults() {
		TeamMember newMember = memberName.getSelectionModel().getSelectedItem();
		return newMember;
	}
}
