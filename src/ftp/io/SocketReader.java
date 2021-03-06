package ftp.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class is a bufferdReader for socket
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class SocketReader extends BufferedReader{	
	public SocketReader(Socket socket) throws IOException{
			super(new InputStreamReader(
					socket.getInputStream()));
	}
	
	public String[] getCommand() throws IOException{
		String commandBlock = this.readLine();
		return commandBlock.split(" ", 2);
	}
}
