package ctrl;

import java.io.IOException;
import java.util.Optional;

import com.daong.db.DBUsers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	TextField email;
	@FXML
	PasswordField password;
	@FXML
	Button newUser;
	@FXML
	DialogPane loginPane;
	
	public String processResults() {
		if (!this.email.getText().isEmpty() && !password.getText().isEmpty()) {
			DBUsers u = new DBUsers();
			return u.login(email.getText(), password.getText()) > 0 ? email.getText(): null;
		}
		return null;
	}
	
	public void newUser() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("新規ユーザー");
		dialog.initOwner(loginPane.getScene().getWindow());
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("../application/newuser.fxml"));
		NewUserController nuc = null;
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			nuc = fxmlLoader.<NewUserController>getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			boolean success = nuc.processResults();
			
		}
	}
}
