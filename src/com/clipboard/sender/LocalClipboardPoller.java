package com.clipboard.sender;

public class LocalClipboardPoller implements Runnable
{
	private static String previousLocalClipboardContents = null;
	
	private String remoteServerHost;
	private int remoteServerPort;
	
	public LocalClipboardPoller(String remoteServerHost, int remoteServerPort)
	{
		this.remoteServerHost = remoteServerHost;
		this.remoteServerPort = remoteServerPort;
	}
	
	@Override
	public void run()
	{
		String localClipboardContents = null;
		
		try
		{
			localClipboardContents = ClipboardUtils.getLocalClipboardContents();
			
			// If the clipboard contents is the same as the previous clipboard 
			// contents, don't do anything
			if (localClipboardContents != null && ! localClipboardContents.equals(previousLocalClipboardContents) )
			{
				// Send the new clipboard contents to the remote server
				ClipboardUtils.setRemoteClipboardContents(localClipboardContents, remoteServerHost, remoteServerPort);
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
