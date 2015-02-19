package ftp.process;

import ftp.FTPClient;

public class ProcessCWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		client.setCurrentDirectory(param[1]);
		client.setAdditionalAnswer(client.getCurrentDirectory());
		return 250;
	}

}
