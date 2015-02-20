package ftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
/**
 * This class represent a FTP server
 * It is the one which wait for connection and create a new FTPClient for every connection
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class FTPServer extends ServerSocket implements Runnable{
	private int nbClients;
	private List<Socket> clientsList;
	
	/**
	 * @return the nbClients
	 */
	public int getNbClients() {
		return nbClients;
	}

	/**
	 * Constructor of a FTPServer.
	 * @param commandPort the port number on which make the connection 
	 * @throws IOException
	 */
	public FTPServer(int commandPort) throws IOException{
		super(commandPort);
		this.nbClients = 0;
		this.clientsList = new LinkedList<Socket>();
	}


	@Override
	public void run() {
		try {			
			while (true){
				/*Client Socket creation*/
				Socket socket = this.accept();				
				FTPClient t =  new FTPClient(socket,this);
				t.start();
			}
			
		} catch (IOException e) {
			System.out.println("Error :"+ e.getMessage());
		}
	}
	
	/**
	 * Accept the connection of a user and open a new socket
	 * @throws IOException  if an I/O error occurs when waiting for a connection.
	 */
	public Socket accept() throws IOException{
		Socket client = super.accept();
		System.out.println("A new client added");
		this.nbClients++;
		System.out.println("NUmber of connexted clients:"+nbClients);
		clientsList.add(client);
		return client;
	}
	
	/**
	 * Remove a client from the clientList when it is disconnected
	 * @param client the client to removed of the clientList
	 */
	public void removeClient(Socket client){
		System.out.println("One client has quit");
		this.nbClients--;
		System.out.println("NUmber of connexted clients:"+nbClients);
		this.clientsList.remove(client);
	}
}
