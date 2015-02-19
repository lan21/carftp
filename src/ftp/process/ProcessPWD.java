package ftp.process;

import ftp.FTPClient;

public class ProcessPWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		client.setAdditionalAnswer(client.getCurrentDirectory());
		return 257;
	}

}
