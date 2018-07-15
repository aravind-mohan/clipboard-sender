# clipboard-sender
Sends local clipboard content to a remote server which will set that content in the clipboard

The idea is to have pollers on both machines, which periodically get the contents of the local clipboard. These pollers will send out 
the contents to the given remote server (if the content has changed since the last poll), and the remote server will set the same 
content in the remote machine's clipboard.
