package ctrl;

import java.io.IOException;
import java.util.Optional;

import application.datamodel.TeamData;
import application.datamodel.TeamMember;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TeamController {
	@FXML
	TableView<TeamMember> tableView;
	@FXML
	TableColumn<TeamMember, Integer> userNumber;
	@FXML
	TableColumn<TeamMember, String> memberName;
	@FXML
	TableColumn<TeamMember, String> email;
	@FXML
	DialogPane teamMembersPane;
	
	TeamData teamData = null;
	
	public void initMembers(TeamData teamData) {
		tableView.setItems(teamData.getMembers());
		this.teamData = teamData;
				
		if (this.teamData.getTeamNumber() > -1) {

			userNumber.setCellValueFactory(new PropertyValueFactory<TeamMember, Integer>("userNumber"));
			memberName.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("name"));
			email.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("email"));

			tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tableView.getSelectionModel().selectFirst();
	
		}		
	}

	public void addToTeam() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("新規タスク追加");
		dialog.initOwner(teamMembersPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../application/addtoteam.fxml"));
		AddTeamController atc = null;
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			atc = fxmlLoader.<AddTeamController>getController();
			atc.initMembers();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			TeamMember newMember = atc.processResults();
			
			teamData.addToTeam(newMember.getEmail());
			
		}
		
	}
}
