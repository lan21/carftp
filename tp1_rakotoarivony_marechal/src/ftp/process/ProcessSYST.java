package ftp.process;

import ftp.FTPClient;
/**
 * Class ProcessSYST
 * This class is used when the client ask for the system version
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessSYST implements ProcessCommand {
	
	public ProcessSYST(){};
	
	@Override
	public int process(String[] param, FTPClient client) {
		return 215;
	}

}
