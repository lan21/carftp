package ftp.process;

import java.io.File;
import java.io.IOException;

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
		try {
			client.getDataWriter().write(result);
			return 226;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 226;
	}

}
