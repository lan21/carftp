package ftp.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	
	
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(2122);
			System.out.println("Serveur créé sur le port 2122");		
			
			while (true){
				System.out.println("En attente de connexion");
				ThreadClient t =  new ThreadClient(s.accept());
				t.start();
			}
			
		} catch (IOException e) {
			System.out.println("Error :"+ e.getMessage());
		}
	}
}
