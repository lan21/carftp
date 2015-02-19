package ftp.process;

import ftp.FTPClient;

public class ProcessTYPE implements ProcessCommand {

	public ProcessTYPE() {}
	
	@Override
	public int process(String[] param, FTPClient client) {
		client.setAdditionalAnswer("Type set to "+param[1]);
		return 200;
	}

}
