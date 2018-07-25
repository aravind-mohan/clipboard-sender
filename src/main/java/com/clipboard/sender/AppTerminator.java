package com.clipboard.sender;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppTerminator implements Runnable
{
	private static final Logger LOGGER = LogManager.getLogger(AppTerminator.class);
	
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
			LOGGER.info("Found " + terminatorFile + " file. Closing the application...");
			System.exit(0);
		}
	}
}
