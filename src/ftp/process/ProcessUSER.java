package ftp.process;

import ftp.main.FTPClient;

public class ProcessUSER implements ProcessCommand {
	
	private String test;
	
	public ProcessUSER(){
		this.test = "test";
	}

	@Override
	public int process(String[] param, FTPClient client) {
		if(param[0].equals(this.test))
			return 331;
		else if(param.equals("anonymous")){
			return 230;
		}
		else
			return 332;
	}

}
