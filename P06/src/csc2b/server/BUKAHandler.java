package csc2b.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class BUKAHandler implements Runnable
{
	
	private Socket socket=null;
	private PrintWriter txtOut=null;
	private BufferedReader  in=null;
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
    public BUKAHandler(Socket newConnectionToClient)
    {	
	//Bind streams
    	try {
			socket=newConnectionToClient;
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
		
    	
    }
    
    @Override
    public void run()
    {
	//Process commands from client	
    	while(true) {
    		try {
    			//read request frrom user 
    			String request=in.readLine();
    			
    			//proccess command
    			if(request.startsWith("AUTH")) {
    				
    				String []data=request.split(" ");
    				
    				boolean isFound=matchUser(data[1],data[2]);
    				
    				if(isFound) {
    					
    					txtOut.println("200 User has been authenticated");
    					txtOut.flush();
    				}else {
    					txtOut.println("500 Server failed to authenticate user");
    					txtOut.flush();
    				}
    				
    			}else if(request.startsWith("LIST")){
    				
    				txtOut.println("200 List has been sent");
    				txtOut.flush();
    				ArrayList<String> list=getFileList();
    				StringBuffer sb=new StringBuffer();
    				for(String line:list) {
    					
    					sb.append(line);
    					sb.append("\t");
    					
    				}
    				txtOut.println(sb.toString());
    				txtOut.flush();

    			}else if(request.startsWith("PDFRET")) {
    				
    				String requestFileID=request.substring(7).trim();
    				String fileName=idToFile(requestFileID);
    				
    				if(!fileName.isEmpty()) {
    					txtOut.println("200 File has been sent");
    					txtOut.flush();
    					
    					byte[] buffer=new byte[1024];
    					int n=0;
    					
    					String filePath="data/server/"+fileName;
    					File fileToSent=new File(filePath);
    					FileInputStream fis=new FileInputStream(filePath);
    					
    					txtOut.println((int)fileToSent.length());
    					txtOut.flush();
    					while((n=fis.read(buffer))>0) {
    						
    						dos.write(buffer,0,n);
    						dos.flush();
    					}
    					fis.close();
    					
    				}else {
    					txtOut.println("500 Could not send the Request File");
    					txtOut.flush();
    				}
    			}else {
    				txtOut.println("500 Something went wrong");
    				txtOut.flush();
    			}
    			
    		} catch (IOException e) {
    			txtOut.println("500 Something went wrong");
    			txtOut.flush();
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    	
    	
    }
    
    private boolean matchUser(String username,String password)
    {
	boolean found = false;
	File userFile = new File("data/server/users.txt"/*OMITTED - Enter file location*/);
	try
	{
		//Code to search users.txt file for match with username and password
	    Scanner scan = new Scanner(userFile);
	    while(scan.hasNextLine()&&!found)
	    {
		String line = scan.nextLine();
		String lineSec[] = line.split("\\s");
    		
		//***OMITTED - Enter code here to compare user*** 
		if(lineSec[0].equals(username)&&lineSec[1].equals(password)) {
			found=true;
		}
		
	    }
	    scan.close();
	}
	catch(IOException ex)
	{
	    ex.printStackTrace();
	}
	
	return found;
    }
    
    private ArrayList<String> getFileList()
    {
		ArrayList<String> result = new ArrayList<String>();
		//Code to add list text file contents to the arraylist.
		File lstFile = new File("data/server/PdfList.txt"/*OMITTED - Enter file location*/);
		try
		{
			Scanner scan = new Scanner(lstFile);

			//***OMITTED - Read each line of the file and add to the arraylist***
			
			while(scan.hasNextLine()) {
				result.add(scan.nextLine());
				
			}
			
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return result;
    }
    
    private String idToFile(String ID)
    {
    	String result = "";
    	//Code to find the file name that matches strID
    	File lstFile = new File("data/server/PdfList.txt"/*OMITTED - Enter file location*/);
    	try
    	{
    		Scanner scan = new Scanner(lstFile);
    		String line = "";
    		//***OMITTED - Read filename from file and search for filename based on ID***
    		while(scan.hasNextLine()) {
    		line=scan.nextLine();
    		
    		if(line.startsWith(ID)) {
    			
    			result=line.substring(2).trim();
    			break;
    		}
    		}
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	return result;
    }
}
