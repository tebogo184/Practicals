package acsse.csc2b.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class LeecherHandler {

	private DatagramSocket clientSocket=null;
	private int portNo;
	private InetAddress Ip;
	private String fileList="";
	
	/**
	 * Constructor that intializes the DatagramSocket
	 */
	public LeecherHandler() {
		
		try {
			clientSocket=new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method responsible for establishing connection with the Seeder
	 * @param portNo-Leecher's port Number 
	 * @param Ip-Leecher's Host name 
	 */
	public void connect(int portNo,InetAddress Ip) {
		
		try {

			//Set up the port Number and the host name 
			this.portNo=portNo;
			this.Ip=Ip;
			
			//Request
			String connect = "Connecting";

			//Send a connection request to the seeder
			DatagramPacket sendConnPacket = new DatagramPacket(connect.getBytes(), connect.length(), Ip, portNo);
			clientSocket.send(sendConnPacket);

			byte[] buffer = new byte[2048];

			//Prepare for the incoming response from the user 
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			clientSocket.receive(receivePacket);
			System.out.println("Connection established");
		} catch (SocketException | UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Method that requests the specified file from the seeder
	 * @param fileID-requested file id 
	 * @param fileList-fileList available,that will be used to extract the requested file Name 
	 */
	public void requestFile(String fileID,String fileList) {
		
				try {
					//files available from the seeder
					this.fileList=fileList;
					
					//Set up the command and the file id 
					String requestFile="FILE "+fileID.trim();
					
					
					//Send the request file command 
					DatagramPacket sendPacket=new DatagramPacket(requestFile.getBytes(),requestFile.length(),Ip,portNo);
					clientSocket.send(sendPacket);
					
			
					//Extract the requested file name  
					StringTokenizer token=new StringTokenizer(fileList,"\n");
					
					//file name that will be used receive the requested file
					String fileName="";
					while(token.hasMoreTokens()) {
						
						//check each line
						String line=token.nextToken();
						//extract the file Id and the file name from the file list
						String id=line.substring(0,1).trim();
						String fName=line.substring(2).trim();
						
						if(fileID.equals(id)) {
							//set the correct name 
						fileName=fName;
						break;
						}
						
					}
					
					//Check if the file is available
					if(!fileName.isEmpty()) {
						//Prepare to read the file sent by the seeder 
						//set the file name 
						FileOutputStream fos=new FileOutputStream(new File("data/"+fileName));
						byte [] fileBuffer=new byte[1024];

						//Read the sent by the seeder
						while(true) {
							//Receive the packet sent by the seeder
							DatagramPacket filePacket =new DatagramPacket(fileBuffer,fileBuffer.length);
							clientSocket.receive(filePacket);
							
							//Check if the file being read is not at the end
							if(filePacket.getLength()<fileBuffer.length) {
								//write the file 
								fos.write(filePacket.getData(),0,filePacket.getLength());
								break;
							}
						//write the file
							fos.write(filePacket.getData(),0,filePacket.getLength());
							
						}
						//close the stream
						fos.close();

						System.out.println("File has been received");
					}else {
						System.out.println("Could not find the file Name");
					}
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
									
			
	}
	
	/**
	 * Method that requests the available file list from the seeder
	 * @return fileList- the list of files that are available
	 */
	public String listRequest() {

		
				try {
					
					//Command to request the list
					String listRequest = "LIST";
					
					//Request the List from the seeder
					DatagramPacket sendPacket = new DatagramPacket(listRequest.getBytes(), listRequest.length(),Ip,portNo);
					clientSocket.send(sendPacket);
					
					
					byte [] buffer=new byte[1024];
					
					//Receive the list from the seeder
					DatagramPacket receivePacket=new DatagramPacket(buffer,buffer.length);
					clientSocket.receive(receivePacket);
					
					//extract the list
					String listReceived=new String(receivePacket.getData(),0,receivePacket.getLength());
					
					//set the list 
					fileList=listReceived;

					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return fileList;
			}
		
	
	
}
