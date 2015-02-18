package ftp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ftp.FTPClient;

public class ProcessUSER implements ProcessCommand {


	@Override
	public int process(String[] param, FTPClient client) {
		if(param[1].equals("anonymous")){
			return 230;
		}
		else {
			Scanner scanfile;
			try {
				scanfile = new Scanner(new File("data/log.txt"));

				while (scanfile.hasNextLine()){
					String[] userPassPath = scanfile.nextLine().trim().split(" ");
					if (userPassPath[0] != param[1]){
						continue;
					}
					else {
						scanfile.close();
						client.setUsername(userPassPath[0]);
						client.setPassword(userPassPath[1]);
						client.setDirectory(userPassPath[2]);
						return 331;
					}
				}
				scanfile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 332;
		}
	}

}