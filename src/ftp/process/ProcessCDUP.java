package ftp.process;

import ftp.FTPClient;

public class ProcessCDUP implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		return (new ProcessCWD().process(param, client));
	}

}
