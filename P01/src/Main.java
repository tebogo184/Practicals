import acsse.gui.EmailClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		//Set up the Pane that will be added to the scene
		EmailClient client=new EmailClient();
		Scene scene=new Scene(client,500,500);
		
		stage.setScene(scene);
		stage.show();
	}

}
