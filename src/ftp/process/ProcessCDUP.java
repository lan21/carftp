package ftp.process;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

public class ProcessCDUP implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		try {
			client.setCurrentDirectory("../"+client.getCurrentDirectory());	
			return 212;
		}
		catch (UnauthorizedChangedDirectoryException e){
			return  553;
		}
	}

}
