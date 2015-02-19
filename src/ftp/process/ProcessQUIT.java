package ftp.process;

import ftp.FTPClient;

public class ProcessQUIT implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		return 221;
	}

}
