package com.clipboard.sender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main
{
	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	
	private static final int CLIPBOARD_POLLER_FIXED_DELAY = 1;
	private static final int TERMINATOR_FILE_FINDER_FIXED_DELAY = 2;
	private static final String TERMINATOR_FILE = ".terminate";
	
	public static void main(String[] args)
	{
		// Get the host and ports from the command-line
		if (args.length != 3)
		{
			printUsageAndExit();
		}
		else if (!(args[0].matches("^\\d+$") && args[2].matches("^\\d+$")))
		{
			printUsageAndExit();
		}
		
		int localServerPort = Integer.parseInt(args[0]);
		String remoteServerHost = args[1];
		int remoteServerPort = Integer.parseInt(args[2]);
		
		// Remove the "terminator" file
		try
		{
			Files.deleteIfExists(Paths.get(TERMINATOR_FILE));
		}
		catch (IOException e)
		{
			LOGGER.fatal(e.getMessage(), e);
		}
		
		// Start the remote clipboard listener
		ExecutorService remoteClipboardService = Executors.newSingleThreadExecutor();
		remoteClipboardService.execute(new RemoteClipboardServer(localServerPort));
		
		// Start the local clipboard listener on a schedule
		ScheduledExecutorService localClipboardPollerService = Executors.newSingleThreadScheduledExecutor();
		localClipboardPollerService.scheduleWithFixedDelay(new LocalClipboardPoller(remoteServerHost, remoteServerPort),
				0, CLIPBOARD_POLLER_FIXED_DELAY, TimeUnit.SECONDS);
		
		// Start the thread which looks for the "terminator" file
		ScheduledExecutorService threadTerminatorService = Executors.newSingleThreadScheduledExecutor();
		
		threadTerminatorService.scheduleWithFixedDelay(new AppTerminator(TERMINATOR_FILE), 0,
				TERMINATOR_FILE_FINDER_FIXED_DELAY, TimeUnit.SECONDS);
	}
	
	private static void printUsageAndExit()
	{
		LOGGER.error("USAGE: java " + Main.class.getName()
				+ " <local_server_port> <remote_server_host> <remote_server_port>");
		
		System.exit(1);
	}
}
