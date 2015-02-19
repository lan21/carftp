package ftp.process;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

public class ProcessCWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		try {
			client.setCurrentDirectory(param[1]);
			client.setAdditionalAnswer(client.getCurrentDirectory());
			return 250;
		} catch (UnauthorizedChangedDirectoryException e) {
			return 553;
		}
		
	}

}
