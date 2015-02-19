package ftp.process;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Date;

import ftp.FTPClient;
import ftp.answer.AnswerBuilder;

public class ProcessLIST implements ProcessCommand {

	@Override
	public int process(String[] param, FTPClient client) {
		if (param.length < 2) {
			File currentDirectory = new File(client.getCurrentDirectory());
			File[] fileList = currentDirectory.listFiles();
			String result = "";
			
			try {
				String partialAnswer = AnswerBuilder.instance.buildAnswer(150,
						"");
				client.getCommandWriter().writeAnswer(partialAnswer);
				result = "total "+fileList.length;
				client.getDataWriter().writeBytes(result);
				client.getDataWriter().flush();

				for (File file : fileList) {
					PosixFileAttributes attr = Files.readAttributes(
							Paths.get(file.getAbsolutePath()),
							PosixFileAttributes.class);
					char dir = '-';
					if (file.isDirectory())
						dir = 'd';
					String chmod = PosixFilePermissions.toString(Files
							.getPosixFilePermissions(file.toPath()));
					String owner = attr.owner().getName();
					String group = attr.group().getName();
					long size = file.length();
					String lastModif = new SimpleDateFormat("MMM dd HH:mm")
							.format(new Date(file.lastModified()));
					String filename = file.getName();

					result = dir + chmod + " " + owner + " " + group + " "
							+ size + " " + lastModif + " " + filename + "\r\n";
					System.out.println(result);
					client.getDataWriter().writeBytes(result);
					client.getDataWriter().flush();
				}

				client.getDataSocket().close();
				return 226;

			} catch (IOException e) {
				return 426;
			}
		} else {
			File file = new File(client.getCurrentDirectory() + '/' + param[1]);
			try {
				PosixFileAttributes attr = Files.readAttributes(
						Paths.get(file.getAbsolutePath()),
						PosixFileAttributes.class);
				char dir = '-';
				if (file.isDirectory())
					dir = 'd';
				String chmod = PosixFilePermissions.toString(Files
						.getPosixFilePermissions(file.toPath()));
				String owner = attr.owner().getName();
				String group = attr.group().getName();
				long size = file.length();
				String lastModif = new SimpleDateFormat("MMM dd HH:mm")
						.format(new Date(file.lastModified()));
				String filename = file.getName();

				String result = dir + chmod + " " + owner + " " + group + " "
						+ size + " " + lastModif + " " + filename + "\r\n";
				System.out.println(result);
				client.getDataWriter().writeBytes(result);
				client.getDataWriter().flush();

				client.getDataWriter().writeBytes(result);
				client.getDataWriter().flush();
				client.getDataSocket().close();
				return 226;
			} catch (IOException e) {
				return 426;
			}
		}
	}

}
