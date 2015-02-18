package ftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import ftp.answer.AnswerBuilder;
import ftp.io.SocketReader;
import ftp.io.SocketWriter;
import ftp.process.ProcessBuilder;
import ftp.process.ProcessCommand;

public class FTPClient extends Thread {
	protected Socket commandSocket;
	protected SocketWriter commandWriter;
	protected SocketReader commandReader;
	protected Socket dataSocket;
	protected DataOutputStream dataWriter;
	protected DataInputStream dataReader;
	private String username;
	private String password;
	private String directory;
	private String additionalAnswer;
	
	public DataInputStream getDataReader() {
		return dataReader;
	}
	
	public Socket getDataSocket() {
		return dataSocket;
	}
	
	public DataOutputStream getDataWriter() {
		return dataWriter;
	}
	
	public void setDataReader(DataInputStream dataReader) {
		this.dataReader = dataReader;
	}
	
	public void setDataSocket(Socket dataSocket) {
		this.dataSocket = dataSocket;
	}
	
	public void setDataWriter(DataOutputStream dataWriter) {
		this.dataWriter = dataWriter;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setAdditionalAnswer(String additionalAnswer) {
		this.additionalAnswer = additionalAnswer;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getDirectory() {
		return this.directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public SocketReader getCommandReader() {
		return commandReader;
	}
	
	public Socket getCommandSocket() {
		return commandSocket;
	}
	
	public SocketWriter getCommandWriter() {
		return commandWriter;
	}
	
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
		this.commandSocket = clientSocket;
		this.commandWriter = new SocketWriter(clientSocket);
		this.commandReader = new SocketReader(clientSocket);
		this.additionalAnswer = "";
	}

	public void processRequest() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		AnswerBuilder answerBuilder = AnswerBuilder.instance;
		try {
			String answer = answerBuilder.buildAnswer(220,this.additionalAnswer);
			this.commandWriter.writeAnswer(answer);
			
			while (!this.commandSocket.isClosed()) {
				
				try {

					String commande[] = this.commandReader.getCommand();
					this.printReceivedCommand(commande);
					ProcessCommand processCommand = processBuilder.processBuild(commande);
					int codeAnswer = processCommand.process(commande, this);
					answer = answerBuilder.buildAnswer(codeAnswer,this.additionalAnswer);
					System.out.println(answer);
					this.commandWriter.writeAnswer(answer);
				}
				catch (ClassNotFoundException e) {
					answer = answerBuilder.buildAnswer(520,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				} 

			}
			this.commandSocket.close();
			this.commandReader.close();
			this.commandWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printReceivedCommand(String[] commande) {
		System.out.print("---->");
		for (int i = 0; i < commande.length; i++) {
			System.out.print(commande[i]+" ");
		}
		System.out.println();
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

	/*public void processPassword(String password)
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
	}*/

	@Override
	public void run() {
		this.processRequest();
	}
}
