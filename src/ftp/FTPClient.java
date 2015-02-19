package ftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import exception.UnauthorizedChangedDirectoryException;
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
	protected User user;
	protected String additionalAnswer;
	protected String currentDirectory;
	protected String apparentDirectory;
	protected FTPServer ftpServer;
	
	
	
	public FTPServer getFtpServer() {
		return ftpServer;
	}

	public String getApparentDirectory() {
		return apparentDirectory;
	}

	public void setApparentDirectory(String apparentDirectory) {
		this.apparentDirectory = apparentDirectory;
	}

	/**
	 * Constructor of a FTPClient. It takes as a parameter a socket on which the client communicates with the FTPServer
	 * @param clientSocket this socket must communicate with the FTPServer
	 * @param ftpServer 
	 * @throws IOException if an I/O error occurs when creating the FTPClient or if the socket is not connected.
	 */
	public FTPClient(Socket clientSocket, FTPServer ftpServer) throws IOException{
		this.ftpServer = ftpServer;
		this.commandSocket = clientSocket;
		this.commandWriter = new SocketWriter(clientSocket);
		this.commandReader = new SocketReader(clientSocket);
		this.additionalAnswer = "";
		this.user = new User("","","",false,false);
	}

	
	
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
	
	public void setCurrentDirectory(String currentDirectory) throws UnauthorizedChangedDirectoryException {
		if(this.currentDirectory !=null){
			if ((!currentDirectory.startsWith(this.getDirectory())&&(!currentDirectory.equals(this.getDirectory())))){
				throw new UnauthorizedChangedDirectoryException();
			}
		}
		this.currentDirectory = currentDirectory;
	}
	
	public String getCurrentDirectory() {
		return currentDirectory;
	}
	
	/**
	 * Return true if write access is granted to the user, false also
 	 * @return true if write access is granted to the user, false also 
	 */
	public boolean hasWriteAccess(){
		return this.user.getWriteAccess();
	}
	
	/**
	 * Return true if read access is granted to the user, false also
 	 * @return true if read access is granted to the user, false also 
	 */
	public boolean hasReadAccess(){
		return this.user.getReadAccess();
	}
	
	/**
	 * Set write access at true if write access is granted if the user
	 * @param access : true if write access is granted if the user
	 * anonymous is never granted to write access
	 */
	public void setWriteAccess(boolean access){
		this.user.setWriteAccess(access);
	}
	
	/**
	 * Set read access at true if write access is granted if the user
	 * @param access : true if read access is granted if the user
	 */
	public void setReadAccess(boolean access){
		this.user.setReadAccess(access);
	}

	public String getPassword() {
		return this.user.getPassword();
	}
	

	/**
	 * Execute the request received by the client
	 */
	public void processRequest() {
		/*Factory used to generate the processCommand depending on the command received by the client*/
		ProcessBuilder processBuilder = new ProcessBuilder();
		/*Factory used to generate the answer depending on the message received by de processCommand*/
		AnswerBuilder answerBuilder = AnswerBuilder.instance;
		try {
			String answer = answerBuilder.buildAnswer(220,this.additionalAnswer);
			this.commandWriter.writeAnswer(answer);
			
			while (!this.commandSocket.isClosed()) {
				
				try {
					/* the command send by the client is analysed */
					String commande[] = this.commandReader.getCommand();
					this.printReceivedCommand(commande);
					/* the appropriate command is generate by the factory */
					ProcessCommand processCommand = processBuilder.processBuild(commande);
					int codeAnswer = processCommand.process(commande, this);
					/* the appropriate answer is generated by the factory */
					answer = answerBuilder.buildAnswer(codeAnswer,this.additionalAnswer);
					System.out.println(answer);
					this.commandWriter.writeAnswer(answer);
				}
				catch(NullPointerException e){
					break;
				}
				/*
				 * Catch the 3 exceptions throwed by the ProcessBuilder
				 * ClassNotFoundException, IllegalAccessException, InstantiationException
				 * Generate the appropriate error message et write it on client terminal
				 */
				catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					answer = answerBuilder.buildAnswer(502,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				}
				this.additionalAnswer = "";
			}
			this.ftpServer.removeClient(this.commandSocket);
			this.commandReader.close();
			this.commandWriter.close();
			this.commandSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * print the command received by the client on the terminal 
	 * @param commande 
	 */
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


}
