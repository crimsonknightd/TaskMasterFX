package ctrl;

import java.util.Calendar;

import application.datamodel.Task;
import application.datamodel.TeamData;
import application.datamodel.TeamMember;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class NewTaskController {
	@FXML
	ComboBox<TeamMember> memberName;
	@FXML
	TextField deadline;
	@FXML
	TextField details;
	
	public Task processResults() {
		Task newTask = new Task();
		
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.YEAR) * 1000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
		
		newTask.setDeadline(Integer.parseInt(deadline.getText()));
		newTask.setDetails(details.getText());
		newTask.setStartDate(today);
		newTask.setTeamNumber(memberName.getSelectionModel().getSelectedItem().getTeamNumber());
		newTask.setUserNumber(memberName.getSelectionModel().getSelectedItem().getUserNumber());
		
		return newTask;
	}
	
	public void initMembers(TeamData teamData) {
				
		ObservableList<TeamMember> members = teamData.getMembers();
		
		memberName.getItems().addAll(members);
	}
}
