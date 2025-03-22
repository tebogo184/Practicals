package acsse.csc2b.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

public class UserPane extends GridPane {

	private int portNo;
	DatagramSocket clientSocket = null;
	private SeederHandler seederHandler = null;
	private LeecherHandler leecherHandler = null;

	// GUI Controls
	private Button seedModeBtn = null;
	private Button leechModeBtn = null;
	TextArea seederFilesList = new TextArea();
	TextField hostTxt = new TextField();
	TextField portTxt = new TextField();
	TextField serverPortNo = new TextField();
	TextField seederResponse = new TextField();

	/**
	 * Constructor that sets up the two modes
	 */
	public UserPane() {

		// instantiate both the leecher and seeder handler
		seederHandler = new SeederHandler();
		leecherHandler = new LeecherHandler();

		seedModeBtn = new Button("Seed Mode");
		leechModeBtn = new Button("Leech Mode");

		this.setAlignment(Pos.CENTER);
		setHgap(5);
		setVgap(5);
		add(seedModeBtn, 0, 0);
		add(leechModeBtn, 1, 0);

		leechModeBtn.setOnAction(e -> {

			// set up the Leecher GUI
			setLeecherMode();
		});

		seedModeBtn.setOnAction(e -> {
			// set up the Seeder GUI
			setSeedMode();
		});

	}

	/**
	 * Method responsible for setting up the Seeder GUI
	 */
	private void setSeedMode() {

		// remove the controls in the scene
		getChildren().clear();

		// Button that allows the seeder to add the file that will be available to share
		Button addFile = new Button("Add File");
		addFile.setOnAction(e -> {

			// allow the user to chooser a file
			FileChooser fc = new FileChooser();
			File newFile = fc.showOpenDialog(null);

			if (newFile != null && newFile.exists()) {

				// Add the new File into the seeder and returns the id for that file
				int id = seederHandler.addFile(newFile);
				// append the file name into Seeder's available files list
				seederFilesList.appendText(id + "\t" + newFile.getName() + "\n");

			}
		});

		seederFilesList.setPrefWidth(400);

		this.setAlignment(Pos.CENTER);

		// Button responsible for starting the DatagramSocket of the Seeders
		Button startServerBtn = new Button("Start Server");
		startServerBtn.setOnAction(e -> {

			// creates a runnable task that will be used in the Thread
			Runnable task = new Runnable() {

				@Override
				public void run() {
					// check if the Seeder has already set the port number and the has added a file
					// that can be shared
					if (!serverPortNo.getText().isEmpty() && !seederFilesList.getText().isEmpty()) {
						int port = Integer.parseInt(serverPortNo.getText());
						// Set the port number before starting the server
						seederHandler.setPortNo(port);
						// start the server
						seederHandler.startServer();
					}
				}

			};
			// Set up the task in the thread
			Thread thread = new Thread(task);
			thread.start();
		});

		// add all the gui in the Seeder's GUI
		seederFilesList.setPromptText("Add new Files");
		serverPortNo.setPromptText("Enter Port Number ");
		add(serverPortNo, 0, 0);
		add(startServerBtn, 1, 0);
		add(seederFilesList, 0, 2);
		add(addFile, 0, 3);

	}

	private void setLeecherMode() {

		// remove all the gui controls in the pane
		getChildren().clear();

		hostTxt.setPromptText("Enter Host Name");

		portTxt.setPromptText("Enter Port Number");

		Button connectBtn = new Button("Connect");

		Button getListBtn = new Button("Get List");
		TextArea listText = new TextArea();
		TextField fileID = new TextField();
		Button getFile = new Button("Get File");
		fileID.setPromptText("Enter File ID");
		seederResponse.setPromptText("Server responses");

		// Handles the connect button click event,When the leecher want to connect to
		// the Seeder
		connectBtn.setOnAction(e -> {
			try {

				// Get the port number and the Host name from the leecher
				portNo = Integer.parseInt(portTxt.getText().trim());
				InetAddress Ip = InetAddress.getByName(hostTxt.getText());
				// connect to the Seeder in order to start data transmittion
				leecherHandler.connect(portNo, Ip);
				seederResponse.setText("Success");

			} catch (UnknownHostException e1) {
				seederResponse.setText("Failed to establish a connection");

				e1.printStackTrace();
			}

		});

		// Button click event that requests the file List from the Seeder
		getListBtn.setOnAction(e -> {

			Runnable task = new Runnable() {

				@Override
				public void run() {
					// Requests the list from the Seeder
					String listReceived = leecherHandler.listRequest();
					// set the list received from the Seeder
					if (!listReceived.isEmpty()) {
						listText.setText(listReceived);
						seederResponse.setText("Success");
					} else {
						seederResponse.setText("Something went wrong");
					}

				}
			};
			Thread thread = new Thread(task);
			thread.start();

		});

		// Button click event that requests a file from the Seeder
		getFile.setOnAction(e -> {

			Runnable task = new Runnable() {
				@Override
				public void run() {

					// Get the file id from the user
					String requestedFileId = fileID.getText();
					if (!requestedFileId.isEmpty()) {
						leecherHandler.requestFile(requestedFileId, listText.getText());
						seederResponse.setText("Success");
					} else {
						seederResponse.setText("Please enter the file id");
					}

				}

			};
			Thread thread = new Thread(task);
			thread.start();
		});

		// add all the leecher controls in the pane
		this.setAlignment(Pos.CENTER);
		add(hostTxt, 0, 0, 1, 1);
		add(portTxt, 1, 0, 1, 1);
		add(connectBtn, 2, 0, 1, 1);
		add(getListBtn, 0, 3, 1, 1);
		add(listText, 1, 3, 1, 1);
		add(getFile, 0, 5, 1, 1);
		add(fileID, 1, 5, 1, 1);
		add(seederResponse, 1, 8);
	}

}
