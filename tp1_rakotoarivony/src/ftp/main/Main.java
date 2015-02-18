package ftp.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ftp.FTPClient;

public class Main {
	
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket s = new ServerSocket(2121);
			System.out.println("Server created on port 2121");		
			System.out.println("Waiting connection...");
			
			while (true){
				/*Client Socket creation*/
				Socket socket = s.accept();
				System.out.println("Connection to : "+s.getInetAddress());
				System.out.println(socket.toString());
				FTPClient t =  new FTPClient(socket);
				t.start();
			}
			
		} catch (IOException e) {
			System.out.println("Error :"+ e.getMessage());
		}
	}
}
