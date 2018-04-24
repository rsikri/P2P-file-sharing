
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnection implements Runnable
{
	private static final String CONNECTION_NAME = "ClientConnection";
	private DataInputStream dis;
	private String serverAddress;
	private DataOutputStream dos;
	WriteLog w = new WriteLog();
	private static final int TIMEOUT = 5000;    
	private static final int RECEIVE_TIMEOUT = 1000;    
	private Socket clientSocket;
	private Connection myConnection;
	private int serverPort;
	private PipedOutputStream pipedOutputStream = new PipedOutputStream();
	
	


	
	public ClientConnection(String serverAddress, int serverPort, Connection myConnection) //throws SocketTimeoutException, IOException
	{
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;

		this.clientSocket = new Socket();
		for(;;)
		{
			try
			{
				this.clientSocket.connect(new InetSocketAddress(this.serverAddress, this.serverPort), TIMEOUT);

				this.dos = new DataOutputStream(clientSocket.getOutputStream());
				this.dis = new DataInputStream(clientSocket.getInputStream());
				break;
			}
			catch (IOException e)
			{

				try{Thread.sleep(500);} catch (InterruptedException e1){/*ignore*/}
			}
		}
		this.myConnection = myConnection;
		System.out.println("Client: connected to server now...");


		try{
			w.TcpConnectionOutgoing(Integer.toString(myConnection.getMyPeerID()), this.serverAddress);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	} 

	
	public ClientConnection(Socket aSocket, Connection myConnection) throws IOException
	{

		this.clientSocket = aSocket;
		this.dos = new DataOutputStream(clientSocket.getOutputStream());
		this.dis = new DataInputStream(clientSocket.getInputStream());
		this.myConnection = myConnection;
		System.out.println("Client is connected to server now...");
	}

	
	
	
	private void receive() throws IOException
	{
		receivelen();
				

		
		
		pipedOutputStream.flush();
		clientBlocker();
	}

   public void send(byte[] data) throws IOException
	{
		dos.write(data);
		dos.flush();
	}


    private void receivebuf(int length)throws IOException
	{  byte[] buffer = new byte[length];
		dis.readFully(buffer);
		pipedOutputStream.write(buffer);

	}

	private void receivelen()throws IOException
	{byte[] lengthBuffer = new byte[4];
		dis.readFully(lengthBuffer);
		int length = Utilities.getInteger_From_Byte(lengthBuffer, 0);
		pipedOutputStream.write(Utilities.getBytes(length));
receivebuf(length);

	}
	
	
	

	@Override
	public void run()
	{
		
		while(true)
		{
			try
			{
				this.receive();
			}
			catch (InterruptedIOException iioex)
			{
				if(myConnection.getBitMap().canIQuit())
				{
					try
					{
						printconnecetionclose();
						
					
						Thread.yield();
						this.closeConnections();
						
					}
					catch (Exception e){}
					break;
				}
			}
			catch (IOException e)
			{
				break;
			}
		}
	}
public void printconnecetionclose()
{
System.out.println("close connections called");
}

	protected void clientBlocker(){
		ArrayList<Integer> one = new ArrayList<Integer>();
		int i=0;
		while(i<10)
		{
			one.add(i);
			i++;
		}
		chckconnection(one);
		
	}

	synchronized void receive(int preknownDataLength) throws EOFException, IOException
	{
		byte[] buffer = new byte[preknownDataLength];
		
		dis.readFully(buffer);
		pipedOutputStream.write(buffer);
	}

protected void chckconnection(ArrayList one)
{
	if(CONNECTION_NAME== "ClientConnection"){
			Collections.sort(one);
		}
}
	
	private void closeConnections() throws IOException
	{
		if(this.pipedOutputStream != null)
		{	
                        System.out.println("close piped OS");
			this.pipedOutputStream.close();
		}
		if(this.dis != null)
		{						
                        System.out.println("close dis");
			this.dis.close();
		}
		if(this.dos != null)
		{						
                        System.out.println("close dos");
			this.dos.close();
		}
		if(this.clientSocket != null)
		{						
                        System.out.println("close cli socket");
			this.clientSocket.close();
		}
	}

	public void setSoTimeout() throws SocketException
	{
		this.clientSocket.setSoTimeout(RECEIVE_TIMEOUT);
	}

	public PipedOutputStream getPipedOutputStream()
	{
		return this.pipedOutputStream;
	}

	
}

