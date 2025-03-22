import acsse.csc2b.gui.UserPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		UserPane root = new UserPane();

		Scene scene = new Scene(root, 500, 500);

		stage.setScene(scene);
		stage.show();
	}

}
