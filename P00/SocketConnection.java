import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * @author Tshukudu T
 */
public class SocketConnection {
	
	private Socket socket=null;
	private final int counter=65535;

	
	/**
	 * This method connects to the socket and tests for open ports
	 * If the port is not closed then we display to the error stream
	 * If the port is open then we connect to that port and display information 
	 * about the port 
	 */
	public void startSocket() {
		
		//THe loop increments by 3
		for(int i=1;i<counter;i+=3) {
		
			try {
				//establish connections on the current i value
				socket=new Socket("localhost",i);
				
				//display information about the port
				System.out.printf("Program connected to localhost: %s\n",i);
				System.out.printf("Local port of the connection: %s\n",socket.getLocalPort());
				System.out.printf("Remote port of the connection: %s\n",socket.getPort());
			} catch (UnknownHostException e) {
				System.err.printf("Could not connect to localhost port: %s\n",i);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.printf("Could not connect to localhost port: %s\n",i);
			}finally{
				if(socket!=null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	/**
	 * Displays the IP Address 
	 */
	public void displayIP() {
		
		try {
			
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
