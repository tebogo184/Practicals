package acsse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

	private final int portNo=4321;
	
	/**
	 * Initalizes the server and wait for connections
	 */
	public void  startServer() {
		try (ServerSocket server = new ServerSocket(portNo)) {
			while(true) {
				Socket socket=server.accept();
				
				
				ClientHandler client=new ClientHandler(socket);
				
				ExecutorService service=Executors.newCachedThreadPool();
				service.execute(client);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}
