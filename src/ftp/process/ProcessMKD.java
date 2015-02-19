package ftp.process;

import java.io.File;

import ftp.FTPClient;

/**
 * Class ProcessMKD
 * Allow to make a new directory in the current directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessMKD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		String path = client.getCurrentDirectory()+"/"+param[1];
		File dir = new File(path);
		if (dir.mkdir()){
			return 212;
		}
		else {
			return 550;
		}
	}
	
}
