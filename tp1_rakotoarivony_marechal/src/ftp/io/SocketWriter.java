package ftp.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * This class is a bufferdwWriter for socket
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class SocketWriter extends BufferedWriter {

	public SocketWriter(Socket socket) throws IOException {
		super(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void writeAnswer(String answer) throws IOException {
		this.write(answer+"\r\n");
		this.flush();
	}

}
