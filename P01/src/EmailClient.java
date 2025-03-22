package acsse.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class EmailClient extends StackPane{

	//GUI variables
	private TextField hostNameField=null;
	private TextField portField=null;
	private TextField senderField=null;
	private TextField recipientField=null;
	private TextField subjectField=null;
	private Label subjectLabel=null;
	private Label recipientLabel=null;
	private Label senderLabel=null;
	private Label hostNameLabel=null;
	private Label portLabel=null;
	private TextArea bodyField=null;
	private GridPane grid=null;
	private Button sendButton=null;
	
	
	//socket variables
	private Socket socket=null;
	private BufferedReader in=null;
	private PrintWriter out=null;
	private int port=0;
	
	

 
	/**
	 * Constructor that will initialize the 
	 */
	public EmailClient() {
		this.hostNameField=new TextField();
		this.portField=new TextField();
		this.senderField=new TextField();
		this.recipientField=new TextField();
		
		this.bodyField=new TextArea();
		this.portLabel=new Label();
		this.hostNameLabel=new Label();
		this.sendButton=new Button("Send Email");
		this.grid=new GridPane();
		this.subjectLabel=new Label();
		this.subjectField=new TextField();
		this.senderField=new TextField();
		this.recipientLabel=new Label();
		this.senderLabel=new Label();
		
		start();//Set up the client interface
	}
	
	
	/**
	 * This method sets up the client interface
	 * All the fields, label and button required to send the email are added to the client interface
	 */
	
	private void start() {
		
		
		grid.setVgap(10);
		grid.setHgap(10);
		//Add the hostName label and the host Label to the grid
		hostNameLabel.setText("Host Name:");
		grid.add(hostNameLabel,0,0);
		grid.add(hostNameField, 1,0);
		
		
		//add the port label and the port Field to the grid
		portLabel.setText("Port Number:");
		grid.add(portLabel, 0, 1);
		grid.add(portField, 1, 1);

		//add the recipient fields and labels to the grid
		recipientLabel.setText("Recipient Email:");
		grid.add(recipientLabel, 0, 2);
		grid.add(recipientField, 1, 2);
		
		//add the sender fields and labels to the grid
		senderLabel.setText("Sender Email:");
		grid.add(senderLabel, 0, 3);
		grid.add(senderField, 1, 3);
		
		//add the subject fields and labels to the grid
		subjectLabel.setText("Subject :");
		grid.add(subjectLabel, 0, 4);
		grid.add(subjectField, 1, 4);
		
		//add the text area to the grid
		grid.add(bodyField, 1, 8);
	
		//add the button to the grid
		grid.add(sendButton, 1, 9);
		this.getChildren().add(grid);
		
		
		//Send button event handler
		sendButton.setOnAction(e->{
			
		sendEmail();//Send the email 
		});
		
	}
	
	/**
	 * This method is responsible for sending the email to recipient
	 * It retrieves all the data required from the client, and establishes connection in order to send the email
	 */
	private void sendEmail() {
		
		try {
			//Get the host name and the port number from the Client
			String host=hostNameField.getText();
			port=Integer.parseInt(portField.getText());
			
			//Establish a connection to
			socket =new Socket(host,port);
			
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream(),true);
			
			

			//initiate a conversation using the helo command 
			out.println("HELO socket.example.org");
			out.flush();
			
			//Get the sender's email from the client and write it to the server
			out.println(String.format("MAIL FROM:<%s>",senderField.getText()));
			out.flush();

			//Get the recipient's email from the client and write it to the server

			out.println(String.format("RCPT TO:<%s>", recipientField.getText()));
			out.flush();

			//Indicate that we are about to send the data
			out.println("DATA");
			out.println();
			
			//Send all the data received from the client
			out.println(String.format("From: \"%s\" <%s>",senderField.getText(),senderField.getText()));
			out.println(String.format("To:\"%s\" <%s>", recipientField.getText(),recipientField.getText()));
			out.println("Cc:");
			out.println(String.format("Date:%s", LocalDate.now()));//Use the current date
			out.println(String.format("Subject:%s", subjectField.getText()));
			out.println("\r\n");
			out.println(bodyField.getText());
			out.println("\r\n.\r\n");
			out.flush();

			//Stop the conversation
			out.println("QUIT\r\n");
			out.flush();
		

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				socket.close();
				in.close();
				out.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	
}
