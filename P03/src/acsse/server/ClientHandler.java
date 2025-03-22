package acsse.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

	private Socket socket = null;
	DataOutputStream out = null;
	private BufferedReader in = null;
	PrintWriter txtOut = null;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * This run method will be responsible for the processing of the clients request
	 * 
	 */
	@Override
	public void run() {

		try {
			// Initialize stream that will be used to read and write data
			txtOut = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// read the request from the client
			String line = in.readLine();

			// extract the request using a string tokenizer
			StringTokenizer token = new StringTokenizer(line, " ");

			// check the method of the request.
			String method = token.nextToken();

			// if the method is a Get, then we process the request .Else we respond with a
			// 404 not found error
			if (method.equals("GET")) {

				// extract the file request by the user
				String fileRequested = token.nextToken();

				// first we check if the type of file requested by the user
				if (fileRequested.equals("/Africa.jpg")) {

					// create the full path for the file
					String path = "data" + fileRequested;

					// create a file with created path
					File file = new File(path);

					// Type of the file requested
					String fileType = "image/jpeg";

					// call the handle file method
					handleFile(file, fileType);

				} else {
					// create the full path for the file
					String path = "data" + fileRequested + ".html";

					// create a file with created path
					File file = new File(path);

					// check if the request file exists.
					if (file.exists()) {

						// Type of the file requested
						String contentType = "text/html";

						// call the handle file method.Else we respond with a 404 not found error
						handleFile(file, contentType);

					} else {

						// specify the error Type
						String errorType = "Http/1.1 404 NOT FOUND";
						// specify the error content to be displayed to the client
						String errorOutput = "<html><body><h1>Not Found</h1></body></html>";

						// Since the file can not be found, we use call the handle error method
						handleError(errorType, errorOutput);

					}
				}

			}
		} catch (IOException e) {

			// specify the error Type
			String errorType = "Http/1.1 500 NOT FOUND";

			// specify the error content to be displayed to the client
			String errorOutput = "<html><body>500 Internal Server Error</h1></body></html>";

			// Since the file can not be found, we use call the handle error method
			handleError(errorType, errorOutput);

			e.printStackTrace();
		} finally {
			// after we done read/writing then we close the streams
			try {
				if (txtOut != null) {
					txtOut.close();
					socket.close();
					in.close();
				}
			} catch (IOException e) {

				String errorType = "Http/1.1 500 NOT FOUND";
				String errorOutput = "<html><body>500 Internal Server Error</h1></body></html>";
				handleError(errorType, errorOutput);

				e.printStackTrace();
			}
		}

	}

	/**
	 * This method is used to handle any type of error that occurred during the
	 * reading/writing
	 * 
	 * @param errorType-       the type of error that occurred
	 * @param errorContent-the error content to be displayed to the client
	 */
	void handleError(String errorType, String errorContent) {

		txtOut.println("Http/1.1 " + errorType);
		txtOut.println("Content-Type: text/html");
		txtOut.println();
		txtOut.println("<html><body><h1>" + errorContent + "</h1></body></html>");
		txtOut.flush();
	}

	/**
	 * This method to process the file request and send/write it back to the user
	 * @param file-the  existing file that was request by the client
	 * @param fileType- outlines what type of content the file has
	 */
	void handleFile(File file, String fileType) {

		try {

			txtOut.println("Http/1.1 200 OK");
			txtOut.println("Content-Type:" + fileType);
			txtOut.println("Content-Length:" + file.length());
			txtOut.println();
			txtOut.flush();

			// initializse the streams that will read and write
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			FileInputStream fileInput = new FileInputStream(file);

			// byte array that will be used to read the binary content
			byte[] buffer = new byte[2048];

			// variable that keeps tracks of the size of the file
			int n = 0;

			// read the file and write to stream
			while ((n = fileInput.read(buffer)) > 0) {

				out.write(buffer, 0, n);
				out.flush();
			}

			// closing the stream after reading and writing
			fileInput.close();
			out.close();
		} catch (IOException e) {

			String errorType = "Http/1.1 500 NOT FOUND";
			String errorOutput = "<html><body>500 Internal Server Error</h1></body></html>";
			handleError(errorType, errorOutput);

			e.printStackTrace();
		}
	}

}
