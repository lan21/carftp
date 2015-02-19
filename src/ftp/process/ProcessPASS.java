package ftp.process;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

public class ProcessPASS implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if(param[1].equals(client.getPassword())){
			client.setReadAccess(true);
			client.setWriteAccess(true);
			try {
				client.setCurrentDirectory(client.getDirectory());
			} catch (UnauthorizedChangedDirectoryException e) {
				return 553;
			}
			return 230;
		}
		return 530;
	}

}
