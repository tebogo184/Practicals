package acsse.csc2b.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ImgClient extends Application {

	public static void main(String[] args) {

		launch(args);
	}

	//Set up the scene and the stage 
	@Override
	public void start(Stage stage) throws Exception {

		ClientPane root = new ClientPane();

		Scene scene = new Scene(root, 500, 500);
		

		stage.setScene(scene);
		stage.show();
	}

}
