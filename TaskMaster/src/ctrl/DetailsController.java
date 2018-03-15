package ctrl;

import application.datamodel.Comment;
import application.datamodel.Task;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DetailsController {
	@FXML
	TableView<Comment> tableView;
	@FXML
	TableColumn<Comment, String> comment;
	@FXML
	TableColumn<Comment, Long> commentTime;
	@FXML
	TableColumn<Comment, String> commentUserName;
	@FXML
	TextField taskNumber;
	@FXML
	TextField memberName;
	@FXML
	TextField startDate;
	@FXML
	TextField deadline;
	@FXML
	TextField details;
	@FXML
	TextField newComment;
	
	
	public void initialize() {
		
	}
	
	public void setData(Task task) {
		
		tableView.setItems(task.getComments());
		
		comment.setCellValueFactory(new PropertyValueFactory<Comment, String>("comment"));
		commentTime.setCellValueFactory(new PropertyValueFactory<Comment, Long>("time"));
		commentUserName.setCellValueFactory(new PropertyValueFactory<Comment, String>("memberName"));

		tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tableView.getSelectionModel().selectFirst();
		
		taskNumber.setText("" + task.getTaskNumber());
		memberName.setText(task.getMemberName());
		startDate.setText("" + task.getStartDate());
		deadline.setText("" + task.getDeadline());
		details.setText(task.getDetails());
	}
	
	public Comment processComment() {
		Comment newCom = new Comment();
		newCom.setComment(newComment.getText());
		return newCom;
	}
}
