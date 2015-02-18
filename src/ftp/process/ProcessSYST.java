package ftp.process;

import ftp.FTPClient;

public class ProcessSYST implements ProcessCommand {
	
	public ProcessSYST(){};
	
	@Override
	public int process(String[] param, FTPClient client) {
		return 215;
	}

}
