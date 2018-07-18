package com.clipboard.sender;

import java.nio.file.Files;
import java.nio.file.Paths;

public class AppTerminator implements Runnable
{
	private String terminatorFile;
	
	public AppTerminator(String terminatorFile)
	{
		this.terminatorFile = terminatorFile;
	}
	
	@Override
	public void run()
	{
		if (Files.exists(Paths.get(terminatorFile)))
		{
			System.exit(1);
		}
	}
}
