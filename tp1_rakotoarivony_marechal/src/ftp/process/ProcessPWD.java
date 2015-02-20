package ftp.process;

import ftp.FTPClient;

/**
 * This class is used to print the current working directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessPWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		client.setAdditionalAnswer('"'+client.getApparentDirectory()+"\" is current working directory.");
		return 257;
	}

}
