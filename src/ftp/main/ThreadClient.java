package ftp.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadClient extends Thread{
	Socket socketClient;
	public ThreadClient(Socket clientSocket) {
		this.socketClient = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
			System.out.println("tr"+is.readLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
