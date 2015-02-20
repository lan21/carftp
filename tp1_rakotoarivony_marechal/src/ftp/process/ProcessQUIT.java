package ftp.process;

import ftp.FTPClient;

/**
 * This class is used when the client quits the server
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessQUIT implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		return 221;
	}

}
