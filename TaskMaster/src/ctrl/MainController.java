package ctrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.daong.db.DBUsers;

import application.datamodel.Comment;
import application.datamodel.Task;
import application.datamodel.TaskData;
import application.datamodel.TeamData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class MainController {
	@FXML
	BorderPane mainBorderPane;
	@FXML
	Menu taskMenu;
	@FXML
	MenuItem newTaskMenu;
	@FXML
	MenuItem taskDetailsMenu;
	@FXML
	MenuItem deleteMenu;
	@FXML
	Menu teamMenu;
	@FXML
	MenuItem showTeamMenu;
	@FXML
	Menu helpMenu;
	@FXML
	MenuItem aboutMenu;
	@FXML
	TableView<Task> tableView;
	@FXML
	TableColumn<Task, Integer> taskNumber;
	@FXML
	TableColumn<Task, String> details;
	@FXML
	TableColumn<Task, String> memberName;
	@FXML
	TableColumn<Task, String> deadline;
	@FXML
	Label email;
	
	private TaskData taskData = null;
	private String emailStr = null;
	private TeamData teamData = null;
	private int userNumber = -1;
	
	public void initialize() {
		taskData = new TaskData(emailStr);		
		taskData.loadTasks();
		this.email.setText(emailStr);
		
		tableView.setItems(taskData.getTasks());
		
		taskNumber.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskNumber"));
		details.setCellValueFactory(new PropertyValueFactory<Task, String>("details"));
		memberName.setCellValueFactory(new PropertyValueFactory<Task, String>("MemberName"));
		deadline.setCellValueFactory(new PropertyValueFactory<Task, String>("deadline"));

		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableView.getSelectionModel().selectFirst();
		
		teamData = new TeamData(taskData.getTeamNumber());
		teamData.loadTeam();
		userNumber = findUserNumber(this.emailStr);
		
		if (teamData.getTeamNumber() < 0) {
			MenuItem newItem = new MenuItem("チーム作成");
			newItem.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					teamData.createTeam(emailStr);
					taskData.setTeamNo(teamData.getTeamNumber());
					taskData.loadTasks();
					teamMenu.getItems().remove(newItem);
				}
				
			});
			
			teamMenu.getItems().add(newItem);
		}
		
		Thread tr = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(1800000); //1800000ms = 30min
						teamData.loadTeam();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		};
		
		tr.setDaemon(true);
		tr.start();
		
	}


	public void showTeam() { 
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("チームメンバー");
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../application/listmembers.fxml"));
		TeamController tc = null;
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			tc = fxmlLoader.<TeamController>getController();
			tc.initMembers(teamData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.showAndWait();
	}

	public void addTask() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("新規タスク追加");
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../application/newtask.fxml"));
		NewTaskController ntc = null;
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			ntc = fxmlLoader.<NewTaskController>getController();
			ntc.initMembers(teamData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK) {
			Task newTask = ntc.processResults();
			taskData.addTask(newTask);
		}
	}
	
	public void showDetails() {
		Task task = tableView.getSelectionModel().getSelectedItem();
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("詳細");
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../application/details.fxml"));
		DetailsController dc = null;
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			dc = fxmlLoader.<DetailsController>getController();
			dc.setData(task);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK) {
			Comment comment = dc.processComment();
			comment.setUserNumber(this.userNumber);
			taskData.addComment(task, comment);	
		}
		
	}
	
	public void deleteTask() {
		Task taskToDelete = (Task)tableView.getSelectionModel().getSelectedItem();
		
		if (taskToDelete != null){
			
			if (confirmation().get() == ButtonType.OK){
				taskData.deleteTask(taskToDelete);
			}
			
		}
		
	}
	
	public void setEmail(String email) {
		this.emailStr = email;
	}
	
	public Optional<ButtonType> confirmation() {
		String title = "確認";
		String header = "本当によろしいでしょうか。";
				
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setContentText(header);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		return dialog.showAndWait();
		
	}
	
	private int findUserNumber(String email) {
		DBUsers u = new DBUsers();
		List<HashMap<String, String>> us = u.getUsers();
		
		for (HashMap<String, String> hm: us) {
			
			if (hm.get("email").equals(email)) {
				return Integer.parseInt(hm.get("user_no"));
			}
			
		}
		
		return -1;
	}
	

}
