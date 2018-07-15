package com.clipboard.sender;

import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteClipboardServer implements Runnable
{
	private int localServerPort;
	
	public RemoteClipboardServer(int serverPort)
	{
		this.localServerPort = serverPort;
	}
	
	@Override
	public void run()
	{
		ServerSocket serverSocket = null;
		
		try
		{
			// Start listening on port
			serverSocket = new ServerSocket(localServerPort, 1);
			
			// Continuously listen for requests
			while (true)
			{
				// For each request, read the incoming clipboard contents
				Socket socket = serverSocket.accept();
				
				InputStreamReader reader = new InputStreamReader(socket.getInputStream());
				
				char[] chars = new char[4096];
				
				StringBuilder stringBuilder = new StringBuilder();
				
				while (reader.read(chars) != -1)
				{
					stringBuilder.append(new String(chars));
				}
				
				try
				{
					reader.close();
				}
				catch(Throwable t)
				{}
				
				try
				{
					socket.close();
				}
				catch(Throwable t)
				{}
				
				// Set the clipboard with those contents
				ClipboardUtils.setLocalClipboardContents(stringBuilder.toString());
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			try
			{
				serverSocket.close();
			}
			catch(Throwable t)
			{}
		}
	}
}
