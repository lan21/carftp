package ftp.process;

import ftp.FTPClient;

/**
 * Class ProcessTYPE
 * This class is used when the clients wants set the type of data transfered
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessTYPE implements ProcessCommand {

	public ProcessTYPE() {}
	
	@Override
	public int process(String[] param, FTPClient client) {
		client.setAdditionalAnswer("Type set to "+param[1]);
		return 200;
	}

}
