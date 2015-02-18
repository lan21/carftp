package ftp.process;

import java.io.File;

import ftp.FTPClient;

public class ProcessGET implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		File directory = new File(client.getDirectory());
		File[] file = directory.listFiles();
		for (int i=0; i<file.length; i++){
			if (file[i].getName().equals(param[1]) && file[i].isFile()){
				client.getDataWriter.writeDate(file[i]);
			}
			else {
				return 550;
			}
		}
		return 125;
	}

}
