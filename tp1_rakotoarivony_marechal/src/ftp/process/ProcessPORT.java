package ftp.process;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ftp.FTPClient;

/**
 * This class is used when the client give the port where he wants to receive data.
 * It sets the passive mode to false
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessPORT implements ProcessCommand {

	@Override
	/**
	 * This method proccess the PORT command
	 * @param param the parameters of the command
	 * @param client the client who want to store file
	 * @return 200 if everthing works fine
	 * 		   425 if the connection cannot be made
	 */
	public int process(String[] param, FTPClient client) {
		String[] adress = param[1].split(",");
		String ipAdress = adress[0]+"."+adress[1]+"."+adress[2]+"."+adress[3];
		int dataPort = Integer.parseInt(adress[4])*256+Integer.parseInt(adress[5]);
		
		try {
			client.setDataSocket(new Socket(ipAdress,dataPort));
			client.setPassiveMode(false);
		} catch (UnknownHostException e) {
			return 425;
		} catch (IOException e) {
			return 425;
		}
		client.setAdditionalAnswer("Port OK");
		return 200;
	}

}
