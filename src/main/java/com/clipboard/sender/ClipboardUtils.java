package com.clipboard.sender;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClipboardUtils
{
	private static final Logger LOGGER = LogManager.getLogger(ClipboardUtils.class);
	
	public static String getLocalClipboardContents() throws Throwable
	{
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		DataFlavor[] availableDataFlavors = systemClipboard.getAvailableDataFlavors();
		
		boolean foundStringDataFlavor = false;
		
		for (DataFlavor dataFlavor : availableDataFlavors)
		{
			if (dataFlavor.equals(DataFlavor.stringFlavor))
			{
				foundStringDataFlavor = true;
				break;
			}
		}
		
		String clipboardContents = null;
		
		if (foundStringDataFlavor)
			clipboardContents = (String) systemClipboard.getData(DataFlavor.stringFlavor);
		
		return clipboardContents;
	}
	
	public static void setLocalClipboardContents(String clipboardContents) throws Throwable
	{
		String operatingSystem = System.getProperty("os.name");
		
		if (operatingSystem != null)
		{
			if (operatingSystem.contains("Linux"))
			{
				String cmd1 = "echo -n '" + clipboardContents + "' | xsel -ip";
				String cmd2 = "echo -n '" + clipboardContents + "' | xsel -is";
				String cmd3 = "echo -n '" + clipboardContents + "' | xsel -ib";
				
				LOGGER.info("Executing " + cmd1);
				Runtime.getRuntime().exec(cmd1);
				LOGGER.info("Executing " + cmd2);
				Runtime.getRuntime().exec(cmd2);
				LOGGER.info("Executing " + cmd3);
				Runtime.getRuntime().exec(cmd3);
			}
			else if (operatingSystem.contains("Windows"))
			{
				String cmd = "\"C:\\Program Files\\Git\\bin\\sh.exe\" --login -i -c "
						+ "\"echo -n '" + clipboardContents + "' | clip\"";
				
				Runtime.getRuntime().exec(cmd);
			}
		}
	}
	
	public static void setRemoteClipboardContents(String clipboardContents, String serverHost, int serverPort) throws Throwable
	{
		Socket socket = new Socket(serverHost, serverPort);
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		writer.write(clipboardContents);
		
		try
		{
			writer.close();
		}
		catch(Throwable t)
		{
			LOGGER.warn(t.getMessage(), t);
		}
		
		try
		{
			socket.close();
		}
		catch(Throwable t)
		{
			LOGGER.warn(t.getMessage(), t);
		}
	}
}
