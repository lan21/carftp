package ftp.main;

import java.io.IOException;
import ftp.FTPServer;

/**
 * Main class of the application
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class Main {

	/**
	 * This is the method called to run the application.
	 * It is initialized with either one argument which is the port number for the server to wait for connection
	 * or with no arguments.
	 * If there is no arguments, the default port is 2121
	 * @param args
	 */
	public static void main(String[] args) {
		FTPServer ftpserver;
		int numPort = 2121;
		if (args.length > 0) {
			try {
				numPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out
						.println("Invalid port number. The connexion will be set on port 2121");
			}
		}

		try {
			ftpserver = new FTPServer(numPort);
			System.out.println("Server opened on the port "+numPort);
			ftpserver.run();
		} catch (IOException e) {
			System.out.println("Initialization impossible: "
					+ e.getMessage());
		}		
	}
}
