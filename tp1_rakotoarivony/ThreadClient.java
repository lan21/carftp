package ftp.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ThreadClient extends Thread{
	Socket socketClient;
	BufferedWriter bw;
	BufferedReader br;

	public ThreadClient(Socket clientSocket) {
		this.socketClient = clientSocket;
		System.out.println("Client thread creation.");
		try{
			this.br = new BufferedReader(new InputStreamReader(this.socketClient.getInputStream()));
			OutputStream ou;
			ou=this.socketClient.getOutputStream(); 
			OutputStreamWriter ouw = new OutputStreamWriter(ou);
			this.bw = new BufferedWriter(ouw);
			/* Là valentin a mis ce code bw.write mais ca ne me fait rien dans mon affichage*/
			char[] ready = "----220 ftp server ready----\n\r".toCharArray();
			bw.write(ready);
			bw.flush();

			/* cette ligne en revanche s'affiche*/
			System.out.println("Client created.");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
	}

	/*tant que a connection n'est pas close...*/
	public void processRequest(){
		try {
			while(!this.socketClient.isClosed()){
				String commandeCourante = this.br.readLine();
				System.out.println(commandeCourante);
				this.userAnalyse(commandeCourante);

			}
			this.socketClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void userAnalyse(String user){
		/* je fais casser le programme si ca fonctionne ^^ */
		if (!user.substring(0, 3).equalsIgnoreCase("user")){
			System.out.println("User ok");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		System.out.println("I'm client");
	}
}
