package ftp.process;

import java.io.IOException;
import java.net.ServerSocket;

import ftp.FTPClient;

/**
 * This class is used when the client ask to be in passive mode
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessPASV implements ProcessCommand {

	@Override
	/**
	 * This method process the PASV command
	 * @param param the parameters of the command
	 * @param client the client who is identifying
	 * @return 227 if OK
	 * 		   501 if there is parameters
	 * 		   425 if connection can't be initialized
	 */
	public int process(String[] param, FTPClient client) {
		if (param.length>1){
			return 501;
		}
		try {
			client.closeDataServerSocket();
			client.setDataServerSocket(new ServerSocket(0));
			String inetAddress = client.getCommandSocket().getInetAddress().getHostAddress();
			System.out.println(inetAddress);
			int port = client.getDataServerSocket().getLocalPort();
			String inetAddressFormated = inetAddress.replace('.', ',');
			int portPart1 = port/256;
			int portPart2 = port%256;
			String answer = "("+inetAddressFormated+","+portPart1+","+portPart2+")";
			System.out.println(answer);
			client.setAdditionalAnswer(answer);
			client.setPassiveMode(true);
			return 227;
		} catch (IOException e) {
			return 425;
		}
	}

}
