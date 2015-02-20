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
		if(client.hasWriteAccess()){
			String path = client.getCurrentDirectory()+"/"+param[1];
			File dir = new File(path);
			if (dir.mkdir()){
				client.setAdditionalAnswer("Directory "+param[1]+" created");
				return 250;
			}
			else {
				client.setAdditionalAnswer("Requested action aborted.");
				return 551;
			}
		}
		else{
			client.setAdditionalAnswer("Creation of directory denied");
			return 532;
		}
	}
	
}
