package acsse.csc2b.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ImgServer {

	public static void main(String[] args) {

		//Allows the server to handle multiple requests 
		try (ServerSocket ss = new ServerSocket(5432)) {
			while (true) {
				Socket socket = ss.accept();
				ClientFileHandler client = new ClientFileHandler(socket);
				Thread thread = new Thread(client);
				thread.start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
