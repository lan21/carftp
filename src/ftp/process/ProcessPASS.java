package ftp.process;

import ftp.main.FTPClient;

public class ProcessPASS implements ProcessCommand {
	
	public ProcessPASS(){
	}

	@Override
	public int process(String[] password, FTPClient client) {
		if(password[0].equals("password")){
			return 230;
		}
		else return 530;
	}

}
