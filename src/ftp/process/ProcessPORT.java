package ftp.process;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ftp.FTPClient;

public class ProcessPORT implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		String[] adress = param[1].split(",");
		String ipAdress = adress[0]+"."+adress[1]+"."+adress[2]+"."+adress[3];
		int dataPort = Integer.parseInt(adress[4])*256+Integer.parseInt(adress[5]);
		
		try {
			client.setDataSocket(new Socket(ipAdress,dataPort));
			client.setDataWriter(new DataOutputStream(client.getDataSocket().getOutputStream()));
			client.setDataReader(new DataInputStream(client.getDataSocket().getInputStream()));
		} catch (UnknownHostException e) {
			return 434;
		} catch (IOException e) {
			return 425;
		}
		
		return 200;
	}

}
