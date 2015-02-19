package ftp.process;

import ftp.FTPClient;

public class ProcessCDUP implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		String newParam[] = {"CWD",".."};
		return (new ProcessCWD().process(newParam, client));
	}

}
