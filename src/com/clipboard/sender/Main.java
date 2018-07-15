package com.clipboard.sender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main
{
	private static final int CLIPBOARD_POLLER_FIXED_DELAY = 1;
	
	public static void main(String[] args)
	{
		// Get the host and ports from the command-line
		if (args.length != 3)
		{
			printUsageAndExit();
		}
		else if ( ! (args[0].matches("^\\d+$") && args[2].matches("^\\d+$")) )
		{
			printUsageAndExit();
		}
		
		int localServerPort = Integer.parseInt(args[0]);
		String remoteServerHost = args[1];
		int remoteServerPort = Integer.parseInt(args[2]);
		
		// Start the remote clipboard listener
		Executors.newSingleThreadExecutor().execute(new RemoteClipboardServer(localServerPort));
		
		// Start the local clipboard listener on a schedule
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleWithFixedDelay(new LocalClipboardPoller(remoteServerHost, remoteServerPort), 0,
				CLIPBOARD_POLLER_FIXED_DELAY, TimeUnit.SECONDS);
	}
	
	private static void printUsageAndExit()
	{
		System.out.println("USAGE: java " + Main.class.getName()
				+ " <local_server_port> <remote_server_host> <remote_server_port>");
		
		System.exit(1);
	}
}
