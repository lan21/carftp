package ftp.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import ftp.FTPServer;
import ftp.io.SocketReader;

public class SocketReaderTest {

	@Test
	public void testSocketReader() {
		try {
			Socket sk = new Socket(InetAddress.getByName(null),2121);
			SocketReader sr = new SocketReader(sk);
			assertNotNull(sk);
			assertNotNull(sr);			
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetCommand() {
		Socket sk;
		try {
			sk = new Socket("localhost",8888);
			SocketReader sr = new SocketReader(sk);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(sk.getOutputStream()));
			br.write("USER myUser is me");
			br.write("PASS password");
			br.flush();
			String[] tab = {"USER","myUser is me"};
			assertArrayEquals("bonne lecture",tab,sr.getCommand());
			sr.close();
			br.close();
		} catch (UnknownHostException e) {
			fail(e.getMessage());;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			fail(e.getMessage());;
		}
		
	}

}
