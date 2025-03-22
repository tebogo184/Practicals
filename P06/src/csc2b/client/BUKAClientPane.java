package csc2b.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BUKAClientPane extends GridPane //You may change the JavaFX pane layout
{
	
	private Socket socket=null;
	private PrintWriter txtOut=null;
	private BufferedReader  in=null;
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
	private boolean isAuth=false;
	
	private ArrayList<String> fileList=new ArrayList<String>();

	//GUI properties
	private Button authBtn=null;
	private Button listBtn=null;
	private Button getFileBtn=null;
	private Button logoutBtn=null;
	private TextField nameField=null;
	private TextField passwordField=null;
	private TextArea textArea=null;
	private TextField fileIDField=null;
	private TextField serverResponseField=null;
	
	
	/**
	 * Constructor that instantiates the member variables
	 */
    public BUKAClientPane()
    {
	
    	
    	 try {
    		//Create client connection
			socket=new Socket("localhost",2018);
			txtOut=new PrintWriter(socket.getOutputStream(),true);
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			dos=new DataOutputStream(socket.getOutputStream());
			dis=new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
    	 
    	 setUpGUI();
    	 
    	 
    	 authBtn.setOnAction(e->{
    		 
    		 try {
    			 serverResponseField.clear();
        		 txtOut.println("AUTH "+nameField.getText()+" "+passwordField.getText());
        		 txtOut.flush();
				String response=in.readLine();
			   
				if(response.startsWith("200")) {
					isAuth=true;
				}
				serverResponseField.setText(response);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		 
    		 
    		 });
    	 
    	 listBtn.setOnAction(e->{
    		 handleListRequest();
    	 });
    	 
    	 
    	 getFileBtn.setOnAction(e->{
    		 
    		 handleFileRequest();
    	 });
    	 
    	 logoutBtn.setOnAction(e->{
    		 
    		 if(socket!=null) {
    			 System.out.println("Connection has been closed ");
    			 try {
					socket.close();
					in.close();
					txtOut.close();
					dis.close();
					dos.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    		 }
    	 });
    	  	
    }
    
    /**
     * This method is finds the File name that the user requested 
     * @param fileID-file ID that is used to find the filename that will be sent
     */
    private String findFileName(String fileID) {
    	
    	//Extract the file name from the list
    	StringTokenizer token=new StringTokenizer(textArea.getText(),"\n");
    	
    	while(token.hasMoreElements()) {
    		
    		String line=token.nextToken();
    		if(line.startsWith(fileID)) {
    			
    			//if the file IDs matches then return the file name
    		  return line.substring(2).trim();
    		 
    		}
    	}
    	
    	//return an empty string if it does not exist
    	return "";
    }

    /**
     * This method sends the file request to the server and handles the response from the server
     */
	private void handleFileRequest() {

		if(isAuth) {
			try {
				 
				//Send the request along with the entered file ID
				txtOut.println("PDFRET "+fileIDField.getText());
				txtOut.flush();
				
				String response=in.readLine();
				
				//Extract the response
				if(response.startsWith("200")) {
					
					//Get the requested file Size 
				String strFileSize=in.readLine();
				int fileSize=Integer.parseInt(strFileSize);
				
				//Prepare to read the file sent from the server 
				byte [] buffer=new byte[1024];
				String fileName=findFileName(fileIDField.getText());
				
				if(!fileName.isEmpty()) {
					
					String filePath="data/client/"+fileName;
					FileOutputStream fos=new FileOutputStream(filePath);
					
					//Read the file
					int totalBytes=0;
					while(fileSize!=totalBytes) {
						
						int n= dis.read(buffer,0,buffer.length);
						
						fos.write(buffer, 0, n);
						
						totalBytes+=n;
						
					}
					
					fos.close();
				}
				}
				
				serverResponseField.setText(response);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	/**
	 * This method send the list request to the server and handles the response from the server 
	 */
	private void handleListRequest() {

		
		if(isAuth) {
			try {
				//
				txtOut.println("LIST");
				txtOut.flush();

				String response=in.readLine();
				if(response.startsWith("200")) {
				serverResponseField.setText(response);
				
				String strList=in.readLine();
				StringTokenizer token=new StringTokenizer(strList,"\t");
				while(token.hasMoreElements()) {
					
					String element=token.nextToken()+"\n";
					fileList.add(element);
					textArea.appendText(element);
				}
				}else if(response.startsWith("400")){
					serverResponseField.setText(response);
				}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}



	private void setUpGUI() {
    	
    	authBtn=new Button("Auth");
    	listBtn=new Button("List");
    	getFileBtn=new Button("Get File");
    	logoutBtn=new Button("Logout");
    	
    	nameField=new TextField();
    	passwordField=new TextField();
    	fileIDField=new TextField();
    	textArea=new TextArea();
    	serverResponseField=new TextField();
    	
    	fileIDField.setPromptText("Enter File ID");
    	nameField.setPromptText("Enter Name");
    	passwordField.setPromptText("Enter Password");
    	this.setAlignment(Pos.CENTER);
    	setHgap(10);
    	setVgap(10);
    	add(nameField,0,0);
    	add(passwordField,1,0);
    	add(authBtn,2,0);
    	add(logoutBtn,3,0);
    	add(listBtn,0,1);
    	add(textArea,1,1);
    	add(getFileBtn,0,2);
    	add(fileIDField,1,2);
    	add(new Label("Server Responses:"),0,4);
    	add(serverResponseField,1,4);
    	
    	
    	
    }
}
