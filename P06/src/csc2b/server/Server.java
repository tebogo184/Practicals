package csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    public static void main(String[] argv)
    {
	//Setup server socket and pass on handling of request
    	
    	try (ServerSocket server = new ServerSocket(2018)) {
			while(true) {
				
				Socket socket=server.accept();
				System.out.println("Connection established");
				BUKAHandler handler=new BUKAHandler(socket);
				Thread thread=new Thread(handler);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
