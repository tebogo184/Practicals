package acsse.csc2b.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class ClientPane extends GridPane {

	private Socket socket = null;
	private final int portNo = 5432;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;

	private Button connBtn;
	private Button uploadBtn;
	private Button getListBtn;
	private Button requestImgBtn;
	private TextArea listTextArea;
	private TextField idField;
	private TextArea responseTextArea;

	private boolean isRunning;
	private String listOfImages = "";
	private int numImages = 3;

	/**
	 * Constuctor that is responsible for setting up the the GUI and settig the
	 * event actions
	 */

	public ClientPane() {
		setUpGUI();

		connBtn.setOnAction(e -> {
			establishConnection();
		});
		uploadBtn.setOnAction(e -> {
			uploadImage();
		});

		getListBtn.setOnAction(e -> {
			getImageList();
		});

		requestImgBtn.setOnAction(e -> {
			downloadImage();
		});

	}

	/**
	 * Helper method that sets up the inital graphical user interface
	 */
	private void setUpGUI() {

		// initialize all the controls
		connBtn = new Button("Connect to Server");
		uploadBtn = new Button("Upload an Image");
		getListBtn = new Button("Get the list of images");
		requestImgBtn = new Button("Download an Image");
		listTextArea = new TextArea();
		idField = new TextField();
		responseTextArea = new TextArea();
		responseTextArea.setPromptText("Responses from server");

		// set the elements of each controls
		responseTextArea.setPrefHeight(100);
		responseTextArea.setPrefWidth(300);
		listTextArea.setPrefHeight(100);
		listTextArea.setPrefWidth(300);
		idField.setPromptText("Enter File ID to retrieve");
		idField.setMaxWidth(200);

		setVgap(5);
		setHgap(5);
		setAlignment(Pos.CENTER);

		// add the controls to the GridPane
		add(connBtn, 0, 0, 1, 1);
		add(getListBtn, 0, 1, 1, 1);
		add(listTextArea, 0, 2, 1, 1);
		add(idField, 0, 3, 1, 1);
		add(requestImgBtn, 1, 3, 1, 1);
		add(uploadBtn, 0, 4, 1, 1);
		add(responseTextArea, 0, 5, 1, 1);

	}

	/**
	 * This helper method is responsible for establishing connection with the server
	 */
	private void establishConnection() {
		try {
			socket = new Socket("localhost", portNo);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

			isRunning = true;

			// read the response from the server
			String response = in.readLine();
			// display the response to the user
			responseTextArea.setText(response);

		} catch (UnknownHostException e1) {
			responseTextArea.setText("Unable to coonnect to Server");
		} catch (IOException e1) {
			responseTextArea.setText("Unable to coonnect to Server");
		}

	}

	/**
	 * Helper method that handles the download image request
	 * 
	 */
	private void downloadImage() {

		if (isRunning) {

			try {
				// Set the file Id
				String fileId = idField.getText();

				// send the command along with the Id to the server
				out.println("DOWN " + fileId);
				out.flush();

				// Extract the server's response
				String response = in.readLine();

				// Check if the request was successful
				if (!response.equals("FAILURE")) {

					// Extract the size of the incoming file
					int fileSize = Integer.parseInt(response);

					String fileName = "";

					// Find the FileName that corresponds with requested Id from the list Of Images
					StringTokenizer token = new StringTokenizer(listOfImages, "\t");
					while (token.hasMoreTokens()) {

						String line = token.nextToken();

						StringTokenizer split = new StringTokenizer(line, " ");
						String tempId = split.nextToken();
						// check if the temp Id and the requested Id are equals
						if (tempId.equals(fileId)) {
							// set the file name once the right id is found
							fileName = split.nextToken();
							break;
						}
					}

					// check if the file is not empty before processing it
					if (!fileName.isEmpty()) {
						// set full file path that will be read
						String filePath = "data/client/" + fileName;
						FileOutputStream fos = new FileOutputStream(filePath);

						byte[] buffer = new byte[2048];
						int n = 0;
						int totalBytes = 0;

						// Use the fileSize to read from the file sent from the server
						while (totalBytes != fileSize) {

							n = dis.read(buffer, 0, buffer.length);
							fos.write(buffer, 0, n);
							totalBytes += n;

						}
						fos.close();// close the stream

						// Set the image
						Image image = new Image("file:" + filePath);
						ImageView imageView = new ImageView();
						// Label the image
						Label imageLabel = new Label("Image View");
						imageView.setImage(image);

						// Add the Label and the ImageView to the Grid Pane
						add(imageLabel, 0, 9, 1, 1);
						add(imageView, 0, 10, 1, 1);

					} else {

						// If the filename is empty then we display this error
						responseTextArea.setText("Unable to find the image requested");
					}

				} else {
					// if the requested failed then we display this error message to the user
					listTextArea.setText(response);
				}

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// If the User has not yet connected to the serve then we display this to them
			responseTextArea.setText("Connect to server first");
		}

	}

	/**
	 * This method is responisble for the Requesting the Image List from the server
	 */
	private void getImageList() {

		// Check if the server is still running
		if (isRunning) {
			try {
				// Send the command to request for the List
				out.println("LIST");
				out.flush();

				listTextArea.clear();

				// Read the response from the Server
				listOfImages = in.readLine();

				// Extract the the list of images sent by the server usin String Tokenizer
				StringTokenizer token = new StringTokenizer(listOfImages, "\t");
				while (token.hasMoreTokens()) {
					listTextArea.appendText(token.nextToken() + "\n");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// if the user has not connected to the server
			responseTextArea.setText("Connect to server first");
		}
	}

	/**
	 * This method is responsible for handling the upload request to the server
	 */
	private void uploadImage() {

		// check if the server is Still running
		if (isRunning) {

			// Allow the user to select their own image
			FileChooser fc = new FileChooser();
			// Set the inital directory
			fc.setInitialDirectory(new File("data/client"));
			// open the dialog
			File selectedFile = fc.showOpenDialog(null);

			// Check if the file is not null and that it exist
			if (selectedFile != null && selectedFile.exists()) {

				// Get the filename
				String fileName = selectedFile.getName();
				// Increment the selected File Id
				int fileId = numImages + 1;

				// Send the request to the server along with the length of the selected file
				out.println("UP " + fileId + " " + fileName + " " + selectedFile.length());
				out.flush();

				// Process the selected file then send it in byte form
				try {

					FileInputStream fis = new FileInputStream(selectedFile);
					byte[] buffer = new byte[2048];
					int n = 0;

					// Send the selected file in bytes form
					while ((n = fis.read(buffer)) != -1) {

						dos.write(buffer, 0, n);
						dos.flush();
					}

					fis.close();// close the stream

					// Read the response from the serve after the file has been sent
					String response = in.readLine();
					// display the response
					responseTextArea.setText(response);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// If the file is null or it does not exist then we display an error to the user
				responseTextArea.setText("FAILURE");
			}

		} else {
			// f the server is not running
			responseTextArea.setText("Connect to server first");
		}

	}

}
