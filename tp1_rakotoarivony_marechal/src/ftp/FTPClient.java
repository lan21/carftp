package ftp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import exception.UnauthorizedChangedDirectoryException;
import ftp.answer.AnswerBuilder;
import ftp.io.SocketReader;
import ftp.io.SocketWriter;
import ftp.process.ProcessBuilder;
import ftp.process.ProcessCommand;

/**
 * This class represent the client which is connected on the server
 * It is initialized with one parameter which is the socket on which it communicates with our server.
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class FTPClient extends Thread {
	/**
	 * Socket on which the server communicates with the client to send and answer commands
	 */
	protected Socket commandSocket;
	
	/**
	 * writer on the commandSocket
	 */
	protected SocketWriter commandWriter;
	
	/**
	 * reader of the commandSocket
	 */
	protected SocketReader commandReader;
	
	/**
	 * Socket on which the server exchange data with the client
	 */
	protected Socket dataSocket;
	
	/**
	 * writer on the dataSocket
	 */
	protected DataOutputStream dataWriter;
	
	/**
	 * reader of the dataSocket
	 */
	protected DataInputStream dataReader;
	
	/**
	 * contains all informations about the client like his access, his folder, his username and password
	 */
	private User user;
	
	/**
	 * this is a string which is used to answer the client depending on his request.
	 */
	private String additionalAnswer;
	
	/**
	 * A string representing the full path of the current directory where the client is.
	 * It is user to create path when the client want to retrieve or to store a file
	 */
	protected String currentDirectory;
	
	/**
	 * A string representing the path that the client sees.
	 * Generally, it is used to hide the real path of the directory where the client is if we want to restrict his access
	 */
	protected String apparentDirectory;
	
	/**
	 * The ftsServer which have accepted this client
	 */
	protected FTPServer ftpServer;
	
	/**
	 * The dataServerSocket used when the client is in passive mode
	 */
	protected ServerSocket dataServerSocket;
	
	/**
	 * the passiveMode is true when the client ask to transfer file with passive mode. It is false when in active mode
	 */
	protected boolean passiveMode;
	
	
	
	public ServerSocket getDataServerSocket() {
		return dataServerSocket;
	}

	public void setDataServerSocket(ServerSocket dataServerSocket) {
		this.dataServerSocket = dataServerSocket;
	}

	public void setPassiveMode(boolean passiveMode) {
		this.passiveMode = passiveMode;
	}
	
	public boolean isPassiveMode() {
		return this.passiveMode;
	}

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
	
	/**
	 * Setter of the current directory
	 * @param currentDirectory
	 * @throws UnauthorizedChangedDirectoryException thrown when he new current directory given in parameter is above the directory of the client
	 */
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
	 * Return true if write access is granted to the user, false if not
 	 * @return true if write access is granted to the user, false if not
	 */
	public boolean hasWriteAccess(){
		return this.user.getWriteAccess();
	}
	
	/**
	 * Return true if read access is granted to the user, false if not
 	 * @return true if read access is granted to the user, false if not
	 */
	public boolean hasReadAccess(){
		return this.user.getReadAccess();
	}
	
	/**
	 * Set write access at true if write access is granted to the user
	 * @param access : true if write access is granted if the user
	 * anonymous is never granted to write access
	 */
	public void setWriteAccess(boolean access){
		this.user.setWriteAccess(access);
	}
	
	/**
	 * Set read access at true if write access is granted to the user
	 * @param access : true if read access is granted if the user
	 */
	public void setReadAccess(boolean access){
		this.user.setReadAccess(access);
	}

	public String getPassword() {
		return this.user.getPassword();
	}
	

	/**
	 * Execute all requests given by the client.
	 * It is an infinite loop which process every requests from the client until he quits.
	 * When the client quits or the connection is broken, the processing stops and this method finish by closing all sockets and reader and writer for this client. 
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
					//System.out.println(answer);
					this.commandWriter.writeAnswer(answer);
				}
				catch(NullPointerException e){
					break;
				}
				catch(IOException e){
					System.out.println("A socket has been disconnected");
					break;
				}
				/*
				 * Catch the 3 exceptions throwed by the ProcessBuilder
				 * ClassNotFoundException, IllegalAccessException, InstantiationException
				 * Generate the error message command not implemented and send it to the client
				 */
				catch (Exception e) {
					answer = answerBuilder.buildAnswer(502,this.additionalAnswer);
					this.commandWriter.writeAnswer(answer);
				}
				this.additionalAnswer = "";
			}
			this.ftpServer.removeClient(this.commandSocket);			
			this.closeDataSocket();
			this.commandReader.close();
			this.commandWriter.close();
			this.commandSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * print the command received from the client on the terminal 
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

	/**
	 * this method close the dataSocket of this FTPClient if not null or if it isn't yet closed.
	 * It close also all reader and writer on the dataSocket
	 * @throws IOException
	 */
	public void closeDataSocket() throws IOException {
		if(dataSocket!=null && !dataSocket.isClosed()){
			this.dataReader.close();
			this.dataWriter.close();
			this.dataSocket.close();
		}
	}
	
	/**
	 * This method close the dataServerSocket of this FTPClient if not null or if it isn't yet closed
	 * @throws IOException
	 */
	public void closeDataServerSocket() throws IOException{
		if(dataServerSocket!=null && !dataServerSocket.isClosed()){
			dataServerSocket.close();
		}
	}


}
