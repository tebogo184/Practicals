import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) {
		
		//instantiate the Socket Connection object
	 SocketConnection conn=new SocketConnection();
	 
	 //test for all open ports
	 conn.startSocket();
	 
	 //Display the IP address
	 conn.displayIP();
	 
	 
	
	}

}
