package acsse.csc2b.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class SeederHandler {

	private DatagramSocket serverSocket = null;
	// Hash map that keeps the Id and the corresponding file that has been added to
	// the list
	private HashMap<Integer, File> hashMap = null;
	private int portNo;

	public SeederHandler() {
		hashMap = new HashMap<Integer, File>();
	}

	/**
	 * Set the port number of the Seeder
	 * 
	 * @param portNo- the Seeder port number
	 */
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	/**
	 * Method responsible for starting the Seeder Datagram Socket
	 */
	public void startServer() {
		try {
			// initalizes the datagramSocket on the specified port number
			serverSocket = new DatagramSocket(portNo);

			System.out.println("Server has started ");
			byte[] data = new byte[1024];

			// Received the connection request from the leecher
			DatagramPacket packet = new DatagramPacket(data, data.length);
			serverSocket.receive(packet);

			// Respond to the connection request
			String response = "Success";
			DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(),
					packet.getAddress(), packet.getPort());
			serverSocket.send(responsePacket);

			// waits for the leecher to make requests
			while (true) {

				byte[] buffer = new byte[1024];

				// Extract the request from the Leecher
				DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
				serverSocket.receive(requestPacket);
				String request = new String(requestPacket.getData(), 0, requestPacket.getLength());

				// check the command from the request and handle accoringly
				if (request.startsWith("FILE")) {

					// Extract the file id of the requested file
					Integer id = Integer.parseInt(request.substring(5));

					// get the file from the hashMap and prepare to share it with the leecher
					File file = hashMap.get(id);

					if (file != null) {
						// Send the File to the Leecher
						try (FileInputStream fis = new FileInputStream(file)) {
							byte[] filebuffer = new byte[1024];
							int n = 0;

							while ((n = fis.read(filebuffer)) != -1) {
								DatagramPacket filePacket = new DatagramPacket(filebuffer, n,
										requestPacket.getAddress(), requestPacket.getPort());

								serverSocket.send(filePacket);
							}

						}

						System.out.println("File list has been sent ");
					}

				} else if (request.startsWith("LIST")) {

					StringBuffer sb = new StringBuffer();

					// loop through each entry and add the file id and the file name in the files
					// available list
					hashMap.forEach((key, file) -> {

						sb.append(key + "\t" + file.getName());
						sb.append("\n");
					});

					// prepare to send the list to the leecher
					byte[] listBuffer = sb.toString().getBytes();
					// send the list to the leecher
					DatagramPacket listPacket = new DatagramPacket(listBuffer, listBuffer.length,
							requestPacket.getAddress(), requestPacket.getPort());
					serverSocket.send(listPacket);

					System.out.println("File List has been sent ");
				} else {
					System.out.println("Invalid request:" + new String(requestPacket.getData()));
				}

			}

		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param newFile-The file that has been added by the Seeder
	 * @return Id - return the added file Id
	 */
	public int addFile(File newFile) {

		int id = hashMap.size() + 1;
		hashMap.put(id, newFile);

		return hashMap.size();
	}

}
