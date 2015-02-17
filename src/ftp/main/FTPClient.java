package ftp.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import exception.UnknownPasswordException;
import exception.UnknownUserException;
import ftp.io.SocketReader;
import ftp.io.SocketWriter;

public class FTPClient extends Thread {
	protected Socket socketClient;
	protected SocketWriter sw;
	protected SocketReader sr;
	private String username;
	private String password;
	private String directory;
	

	/*public FTPClient(Socket clientSocket) {
		this.socketClient = clientSocket;
		this.password = "test";
		this.username = "test123";

		System.out.println("Client thread creation.");
		try {
			this.br = new BufferedReader(new InputStreamReader(
					this.socketClient.getInputStream()));
			OutputStream ou;
			ou = this.socketClient.getOutputStream();
			OutputStreamWriter ouw = new OutputStreamWriter(ou);
			this.bw = new BufferedWriter(ouw);
			char[] ready = "220\r\n".toCharArray();
			bw.write(ready);

			System.out.println("avant flush");

			bw.flush();

			/* cette ligne en revanche s'affiche 
			System.out.println("Client created.");

		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}*/
	
	public FTPClient(Socket clientSocket) throws IOException{
		this.socketClient = clientSocket;
		this.sw = new SocketWriter(clientSocket);
		this.sr = new SocketReader(clientSocket);
	}

	public void processRequest() {
		try {
			while (!this.socketClient.isClosed()) {
				String commandeCourante = this.sr.readLine();
				System.out.println(commandeCourante);
				try {
					this.processUser(commandeCourante);
					
					/*commandeCourante = this.br.readLine();
					System.out.println("  " + commandeCourante);
					this.processSyst(commandeCourante);*/
					
					commandeCourante = this.sr.readLine();
					System.out.println("  " + commandeCourante);
					this.processPassword(commandeCourante);
					
					sw.write("Bienvenue " + this.username + "\n");
					sw.flush();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} 

			}
			this.socketClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*public void processRequest() {
		try {
			while (!this.socketClient.isClosed()) {				
				try {
					String commandeCourante = this.br.readLine();
					System.out.println("recu : "+commandeCourante);					
					bw.write("331\r\n".toCharArray());
					bw.flush();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} 

			}
			this.socketClient.close();
			this.interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public void processPassword(String password)
			throws UnknownPasswordException, IOException {
		System.out.println(password.substring(0, 4));
		if (!password.substring(0, 4).equalsIgnoreCase("PASS")) {
			System.out.println("Password not ok");
			try {
				sw.write("331 User name okay, need password.".toCharArray());
				sw.flush();
			} catch (IOException e) {
				throw new UnknownPasswordException("Server error");
			}

		} else {
			//System.out.println("Password ok");
			if (!password.substring(5).equals(this.password)) {
				throw new UnknownPasswordException(
						"331 User name okay, need password.\n");
			}
			else{
				sw.write("200 OK. user logged in\r\n".toCharArray());
				sw.flush();
			}
		}

	}

	public void processUser(String user) throws UnknownUserException,
			IOException {
		System.out.println(user.substring(0, 4));
		if (!user.substring(0, 4).equalsIgnoreCase("user")) {
			System.out.println("User not ok");
			throw new UnknownUserException("");
		} else {
			System.out.println(user.substring(5));
			System.out.println(!user.substring(5).equals(this.username));
			if (!user.substring(5).equals(this.username)) {
				throw new UnknownUserException("");
			} else {
				System.out.println("User ok");
				sw.write("331 User name okay, need password.\n");
				sw.flush();
			}
		}
	}

	public void processSyst(String user) throws UnknownUserException,
			IOException {
		System.out.println(user.substring(0, 4));
		if (!user.substring(0, 4).equalsIgnoreCase("SYST")) {
			throw new UnknownUserException("");
		} else {
			System.out.println("Syst ok");
			sw.write("215 Unix.\n\r");
			sw.flush();
		}
	}

	@Override
	public void run() {
		this.processRequest();
	}
}
