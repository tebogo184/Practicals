import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;




/**
 * @author Tshukudu T
 */
public class Main {

	public static void main(String[] args) {


		//instantiate a server socket along with port number
		try (ServerSocket server = new ServerSocket(8888)) {
			//wait for a client to establish a connection
			Socket socket =server.accept();
			
			//Pass the socket to the client handler
			ClientHandler client=new ClientHandler(socket);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
