package ctrl;

import com.daong.db.DBUsers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class NewUserController {
	@FXML
	TextField email;
	@FXML
	PasswordField password1;
	@FXML
	PasswordField password2;
	@FXML
	TextField name;
	
	public boolean processResults() {
		if (!this.email.getText().isEmpty() && !password1.getText().isEmpty() && !password2.getText().isEmpty() && !name.getText().isEmpty()) {
			
			String p1, p2;
			p1 = password1.getText();
			p2 = password2.getText();
			if (p1.equals(p2)) {
				return addMember(email.getText(), p1, name.getText());
			} 
		}
		return false;
	}
	
	public boolean addMember(String email, String password, String name) {
		DBUsers db = new DBUsers();
		boolean success = db.addMember(email, password, name, 0);
		success = db.addToTeam(email, -1);
		return success;
	}
}
