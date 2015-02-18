package ftp.process;

import ftp.FTPClient;

public class ProcessPASV implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		// TODO Auto-generated method stub
		return 227;
	}

}
