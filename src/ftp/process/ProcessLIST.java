package ftp.process;

import java.io.File;

import ftp.FTPClient;

public class ProcessLIST implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		File directory = new File(client.getDirectory());
		File[] fileList = directory.listFiles();
		String result = "";
		for (File f : fileList){
			result += f.getName()+"\n";
		}
		client.getSw().writeAnswer(result);
		return 226;
	}

}
