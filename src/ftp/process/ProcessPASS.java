package ftp.process;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

/**
 * Class ProcessPASS
 * This class is used when the client is identifying
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessPASS implements ProcessCommand {

	@Override
	/**
	 * This method process the PASS command
	 * @param param the parameters of the command
	 * @param client the client who is identifying
	 * @return 230 if OK
	 * 		   530 if the password is wrong
	 */
	public int process(String[] param, FTPClient client) {
		if(param[1].equals(client.getPassword())){
			try {
				client.setCurrentDirectory(client.getDirectory());
			} catch (UnauthorizedChangedDirectoryException e) {	}
			client.setReadAccess(true);
			client.setWriteAccess(true);
			return 230;
		}
		return 530;
	}

}
