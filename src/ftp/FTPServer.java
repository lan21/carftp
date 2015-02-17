package ftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import ftp.main.FTPClient;

public class FTPServer extends ServerSocket implements Runnable{
	private int nbClients;
	private List<Socket> listeClient;
	
	public FTPServer(int commandePort,int dataPort) throws IOException{
		super(commandePort);
		this.nbClients = 0;
		this.listeClient = new LinkedList<Socket>();
	}
	
	public void execute(){
		
	}

	@Override
	public void run() {
		try {			
			while (true){
				/*Client Socket creation*/
				Socket socket = this.accept();
				FTPClient t =  new FTPClient(socket);
				t.start();
			}
			
		} catch (IOException e) {
			System.out.println("Error :"+ e.getMessage());
		}
	}
	
	public Socket accept() throws IOException{
		Socket client = super.accept();
		this.nbClients++;
		listeClient.add(client);
		return client;
	}
	
	public void removeClient(Socket client){
		this.nbClients--;
		this.listeClient.remove(client);
	}
}
