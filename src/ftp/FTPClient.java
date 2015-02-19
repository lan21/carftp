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
import ftp.user.User;

public class FTPClient extends Thread {
	protected Socket commandSocket;
	protected SocketWriter commandWriter;
	protected SocketReader commandReader;
	protected Socket dataSocket;
	protected DataOutputStream dataWriter;
	protected DataInputStream dataReader;
	protected User user;
	protected String additionalAnswer;
	protected String currentDirectory; 
	
	
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
	
	public void setDataSocket(Socket dataSocket) throws IOException {
		this.dataSocket = dataSocket;
		this.dataReader = new DataInputStream(dataSocket.getInputStream());
		this.dataWriter = new DataOutputStream(dataSocket.getOutputStream());
	}
	
	public void setDataWriter(DataOutputStream dataWriter) {
		this.dataWriter = dataWriter;
	}
	
	public void setUser(String username,String password,String directory,boolean writeAccess,boolean readAccess) {
		this.user = new User(username, password, directory,writeAccess,readAccess);
	}
	
	public void setAdditionalAnswer(String additionalAnswer) {
		this.additionalAnswer = additionalAnswer;
	}
	
	public String getUsername() {
		return this.user.getUsername();
	}
	
	public String getDirectory() {
		return this.user.getDirectory();
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
	
	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	
	public String getCurrentDirectory() {
		return currentDirectory;
	}
	
	public boolean hasWriteAccess(){
		return this.user.getWriteAccess();
	}
	
	public boolean hasReadAccess(){
		return this.user.getReadAccess();
	}
	
	public void setWriteAccess(boolean access){
		this.user.setWriteAccess(access);
	}
	
	public void setReadAccess(boolean access){
		this.user.setReadAccess(access);
	}
	
	/**
	 * Constructor of a FTPClient. It takes as a parameter a socket on which the client communicates with the FTPServer
	 * @param clientSocket this socket must communicate with the FTPServer
	 * @throws IOException if an I/O error occurs when creating the FTPClient or if the socket is not connected.
	 */
	public FTPClient(Socket clientSocket) throws IOException{
		this.commandSocket = clientSocket;
		this.commandWriter = new SocketWriter(clientSocket);
		this.commandReader = new SocketReader(clientSocket);
		this.additionalAnswer = "";
		this.user = new User("","","",false,false);
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
					answer = answerBuilder.buildAnswer(502,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				} catch (InstantiationException e) {
					answer = answerBuilder.buildAnswer(502,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				} catch (IllegalAccessException e) {
					answer = answerBuilder.buildAnswer(502,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				}
				this.additionalAnswer = "";

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

	@Override
	public void run() {
		this.processRequest();
	}

	public String getPassword() {
		return this.user.getPassword();
	}
}
