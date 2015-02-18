package ftp.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	
	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket s = new ServerSocket(2124);
			System.out.println("Server created on port 2124");		
			System.out.println("Waiting connection...");
			
			while (true){
				/*Client Socket creation*/
				Socket socket = s.accept();
				System.out.println("Connection to : "+s.getInetAddress());
				System.out.println(socket.toString());
				ThreadClient t =  new ThreadClient(socket);
				t.start();
			}
			
		} catch (IOException e) {
			System.out.println("Error :"+ e.getMessage());
		}
	}
}
