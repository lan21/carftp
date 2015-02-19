package ftp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ftp.FTPClient;

public class ProcessUSER implements ProcessCommand {


	@Override
	public int process(String[] param, FTPClient client) {
		if(param[1].equals("anonymous")){
			client.setUser("anonymous","","/home/rakotoarivony/developpement/CAR/tp1/ftpFolder/sampleFolder",false,true);
			client.setCurrentDirectory(client.getDirectory());
			return 230;			
		}
		else {
			Scanner scanfile;
			try {
				scanfile = new Scanner(new File("data/log.txt"));

				while (scanfile.hasNextLine()){
					String[] userPassPath = scanfile.nextLine().trim().split(" ");
					if (!userPassPath[0].equals(param[1])){
						continue;
					}
					else {
						scanfile.close();
						client.setUser(userPassPath[0],userPassPath[1],userPassPath[2],false,false);
						return 331;
					}
				}
				scanfile.close();
			} catch (FileNotFoundException e) {
				return 451;
			}
			return 430;
		}
	}

}