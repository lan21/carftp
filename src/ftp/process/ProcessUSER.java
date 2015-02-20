package ftp.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import exception.UnauthorizedChangedDirectoryException;
import ftp.FTPClient;

/**
 * Class ProcessUSER
 * This class is used when the client is identifying
 * @author Tanguy Maréchal, Allan Rakotoarivony
 *
 */
public class ProcessUSER implements ProcessCommand {


	@Override
	/**
	 * This method proccess the USER command
	 * if the user is anonymous, it has read-only permissions
	 * @param param the parameters of the command
	 * @param client the client who is identifying
	 * @return 230 if anonymous
	 * 		   331 if registered
	 * 		   430 if not registered
	 * 		   434 if there is an error while reading the database
	 */
	public int process(String[] param, FTPClient client) {
		if(param[1].equals("anonymous")){
			client.setUser("anonymous","","/home/rakotoarivony/developpement/CAR/tp1/ftpFolder/sampleFolder",false,true);
			try {
				client.setCurrentDirectory(client.getDirectory());				
			} catch (UnauthorizedChangedDirectoryException e) {}
			client.setApparentDirectory("/");
			return 230;			
		}
		else {
			Scanner scanfile;
			try {
				scanfile = new Scanner(new File("utils/log.txt"));

				while (scanfile.hasNextLine()){
					String[] userPassPath = scanfile.nextLine().trim().split(" ");
					if (!userPassPath[0].equals(param[1])){
						continue;
					}
					else {
						scanfile.close();
						client.setUser(userPassPath[0],userPassPath[1],userPassPath[2],false,false);
						client.setApparentDirectory("/");
						return 331;
					}
				}
				scanfile.close();
			} catch (FileNotFoundException e) {
				return 434;
			}			
			return 430;
		}
	}

}