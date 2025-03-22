

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientHandler {

	private Socket socket = null;

	/**
	 * This is parameterized constructor
	 * @param socket client that the server will be communicating with
	 */
	public ClientHandler(Socket socket) {

		this.socket = socket;
		
		//start method that will start the weather update process
		start();

	}

	/**
	 * This method is responsible for providing weather updates to the user based on
	 * their request
	 */
	private void start() {

		BufferedReader in = null;
		PrintWriter out = null;

		try {

			// streams to send and receive data
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			// random class
			Random random = new Random();

			int counter = 1;
			// Initiate a conversion with the client
			out.println(String.format("%s HELLO- you may make 4 request and I'LL provide weather information", counter));

			// read the response from the client
			String read = in.readLine();

			if (read.startsWith("START")) {

				out.println(String.format("%s. REQUEST OR DONE", ++counter));

				int i = 0;

				// Allow the client to make four request and reply to each of the request in the
				// loop
				while (i < 4) {

					// Read the request from the user
					String request = in.readLine();

					// evaluates the user's request and respond respectively
					if (request.contains("Johannesburg")) {

						String response = "Clear Skies in Joburg";
						out.println(String.format("%s[%s]", ++counter, response));
						out.flush();
						i++;


					} else if (request.contains("Durban")) {

						String response = "Sunny and Warm in Durban";
						out.println(String.format("%s[%s]", ++counter, response));
						out.flush();
						i++;


					} else if (request.contains("Cape Town")) {

						String response = "Cool and cloudy in Cape Town";
						out.println(String.format("%s[%s]", ++counter, response));
						out.flush();
						i++;


					} else if (request.equalsIgnoreCase("DONE")|| i>4) {

						break;

					} else {
						// If the request is invalid then the response will be randomized using the
						// random class
						String[] responses = { "No data available", "Please try again later",
								String.format("[%s] location data outdated", request) };
						int n = random.nextInt(3);
						out.println(String.format("%s. %s", ++counter, responses[n]));
						out.flush();
						i++;

					}
					

				}
				
				//Notify the user about the number of queries answered
				out.println(String.format("%s OK BYE-[%s]queries answered", ++counter, i));
				out.flush();

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
