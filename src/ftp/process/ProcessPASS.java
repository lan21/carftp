package ftp.process;

import ftp.FTPClient;

public class ProcessPASS implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if(param[1].equals(client.getPassword())){
			client.setReadAccess(true);
			client.setWriteAccess(true);
			client.setCurrentDirectory(client.getDirectory());
			return 230;
		}
		return 530;
	}

}
