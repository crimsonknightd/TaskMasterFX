package application;
	
import java.io.IOException;
import java.util.Optional;

import ctrl.MainController;
import ctrl.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//ログインダイアログで3回失敗したらアプリ終了
		String title = "サイン イン";
		String header = "メールアドレスとパスワードを入力してください";
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		FXMLLoader fxmlloader = new FXMLLoader();
		fxmlloader.setLocation(getClass().getResource("login.fxml"));
		try{
			dialog.getDialogPane().setContent(fxmlloader.load());
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
		
		for (int i = 0; i < 3; i++) {
			Optional<ButtonType> result = dialog.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK){

				LoginController LController = fxmlloader.getController();
				String email = LController.processResults();
				
				if (email != null) {
					try {
						//task.fxmlのメールアドレスを設定するため
						//FXMLLoader.load(getClass().getResource("tasks.fxml"));
						//を使わず、FXMLLoaderのをインスタンス化してからParentに渡す
						FXMLLoader fxmlLoader = new FXMLLoader();
						fxmlLoader.setLocation(getClass().getResource("tasks.fxml"));
						MainController controller = new MainController();
						controller.setEmail(email);
						fxmlLoader.setController(controller);
						Parent root = fxmlLoader.load();
						
						Scene scene = new Scene(root,400,400);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("タスク マスター");
						primaryStage.show();
						
					} catch(Exception e) {
						e.printStackTrace();
					}
					i = 4;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
