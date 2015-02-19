package ftp.main;

import java.io.IOException;
import ftp.FTPServer;

public class Main {

	public static void main(String[] args) {
			FTPServer ftpserver;
			try {
				ftpserver = new FTPServer(2121, 2122);
				ftpserver.execute();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
	}
}
