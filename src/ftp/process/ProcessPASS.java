package ftp.process;

import ftp.FTPClient;

public class ProcessPASS implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if(param[1].equals(client.getPassword())){
			return 230;
		}
		else return 530;
	}

}
