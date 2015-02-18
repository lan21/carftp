package ftp.process;

import ftp.FTPClient;

public class ProcessCWD implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		client.setDirectory(param[1]);
		return 212;
	}

}
