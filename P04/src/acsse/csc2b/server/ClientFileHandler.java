package acsse.csc2b.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientFileHandler implements Runnable {

	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private boolean isRunning;

	/**
	 * Constructor that is responsible for the instantiation of the streams and the
	 * socket
	 * 
	 * @param socket - Client that established connection
	 */
	public ClientFileHandler(Socket socket) {

		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {

			isRunning = true;

			// Send the response to the server
			out.println("Connection sucessful");
			out.flush();
			// While the server is running
			while (isRunning) {

				// Read the request from the Client
				String requestInfo = in.readLine();

				// Check what the command starts with and handling accordingly
				if (requestInfo.startsWith("UP")) {
					handleUploadRequest(requestInfo);
				} else if (requestInfo.startsWith("DOWN")) {
					handleDownloadRequest(requestInfo);
				} else if (requestInfo.startsWith("LIST")) {
					handleListRequest();
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Helper method that is Responsible for handling the List Request
	 */
	private void handleListRequest() {

		StringBuffer sb = new StringBuffer();

		// Read the list from the Image List text file
		File file = new File("data/server/ImgList.txt");

		try (Scanner sc = new Scanner(file)) {

			while (sc.hasNextLine()) {

				String line = sc.nextLine();

				// add each file information to the string buffer
				sb.append(line);
				sb.append("\t");

			}
			// send the list of images information to the client
			out.println(sb.toString());
			out.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param requestInfo- Information about the request including the command
	 */
	private void handleDownloadRequest(String requestInfo) {

		try {

			// Remove the command and asign the ID
			String Id = requestInfo.substring(5);
			String imgName = " ";
			// Scanner that will extract the list of images

			Scanner sc = new Scanner(new File("data/server/ImgList.txt"));

			while (sc.hasNext()) {

				String line = sc.nextLine();

				StringTokenizer token = new StringTokenizer(line, " ");

				String tempId = token.nextToken();

				// Find the Id that will correspond with the requested images Id
				if (tempId.equals(Id)) {
					// Assign the image Name of the requested Image
					imgName = token.nextToken();

					break;
				}
			}

			// Assign the full filePath
			String filePath = "data/server/" + imgName;
			File file = new File(filePath);

			// If the file Exist the we send the file length and the file in bytes form
			if (file.exists()) {
				// Send the file Size
				out.println(file.length());

				FileInputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[2048];

				int n = 0;

				// Send the file in byte form
				while ((n = fis.read(buffer)) != -1) {

					dos.write(buffer, 0, n);
					dos.flush();
				}

				fis.close();
			} else {
				// Else we display an error response
				dos.writeBytes("FAILURE");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Handles the Upload Request
	 * 
	 * @param requestInfo - Information about the request made by the user
	 */
	private void handleUploadRequest(String requestInfo) {

		// Removes the command
		requestInfo = requestInfo.substring(3);

		try {
			// Extract the rest of the information of the request
			StringTokenizer token = new StringTokenizer(requestInfo, " ");

			String fileId = token.nextToken();

			String fileName = token.nextToken();

			// extract the file size
			int fileSize = Integer.parseInt(token.nextToken());

			// Assign the full file Path
			String filePath = "data/server/" + fileName;

			FileOutputStream fos = new FileOutputStream(filePath);
			byte[] buffer = new byte[2048];
			int n = 0;
			int totalBytes = 0;

			// Read the file sent by the client
			while (totalBytes != fileSize) {
				n = dis.read(buffer, 0, buffer.length);
				fos.write(buffer, 0, n);
				totalBytes += n;
			}

			// Respond to the client
			out.println("SUCCESS");
			fos.close();

		} catch (IOException e) {
			// If it the request failed then we send the error message
			out.println("FAILURE");
			e.printStackTrace();
		}

	}
}
