package ftp.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketWriter extends BufferedWriter {

	public SocketWriter(Socket socket) throws IOException {
		super(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public void writeAnswer(String answer) throws IOException{
		this.write(answer+"\r\n");
	}

}
