package ftp.process;

import java.io.File;

import ftp.FTPClient;

/**
 * Class ProcessRMD
 * Allow to make delete a directory
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessRMD implements ProcessCommand {

	@Override
	/**
	 * This method proccess the RMD command
	 * @param param the parameters of the command
	 * @param client the client who who execute the command
	 * @return 250 if everthing works fine
	 * 		   532 if the client has no right to delete the directory
	 * 		   550 if the directory can't be deleted
	 */
	public int process(String[] param, FTPClient client) {
		if(client.hasWriteAccess()){
			String path = client.getCurrentDirectory()+"/"+param[1];
			File dir = new File(path);
			if (dir.delete()){
				client.setAdditionalAnswer("Directory "+param[1]+" deleted");
				return 250;
			}
			else {
				client.setAdditionalAnswer("Cannot delete directory.");
				return 550;
			}
		}
		else{
			client.setAdditionalAnswer("Must have an account to delete file");
			return 532;
		}
	}
	
}
