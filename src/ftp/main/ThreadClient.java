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

public class ThreadClient extends Thread {
	protected Socket socketClient;
	protected BufferedWriter bw;
	protected BufferedReader br;
	private String username;
	private String password;

	public ThreadClient(Socket clientSocket) {
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

			/* cette ligne en revanche s'affiche */
			System.out.println("Client created.");

		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}



	public void processRequest() {
		try {
			while (!this.socketClient.isClosed()) {
				String commandeCourante = this.br.readLine();
				System.out.println(commandeCourante);
				try {
					this.processUser(commandeCourante);
					
					/*commandeCourante = this.br.readLine();
					System.out.println("  " + commandeCourante);
					this.processSyst(commandeCourante);*/
					
					commandeCourante = this.br.readLine();
					System.out.println("  " + commandeCourante);
					this.processPassword(commandeCourante);
					
					bw.write("Bienvenue " + this.username + "\n");
					bw.flush();
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
				bw.write("331 User name okay, need password.".toCharArray());
				bw.flush();
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
				bw.write("200 OK. user logged in\r\n".toCharArray());
				bw.flush();
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
				bw.write("331 User name okay, need password.\n");
				bw.flush();
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
			bw.write("215 Unix.\n\r");
			bw.flush();
		}
	}

	@Override
	public void run() {
		this.processRequest();
	}
}
