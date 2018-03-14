import java.io.*;


public class HandshakeMessage extends Message 
{
	private static final long serialVersionUID = 2L;
	
	final String HANDSHAKE_MSG_HEADER = "P2PFILESHARINGPROJ"; 
	int peerID;
	
	public int getPeerID()
	{
		return peerID;
	}
	
	
	public HandshakeMessage(int peerid) throws InterruptedException, IOException
	{		
		ByteArrayOutputStream baos = Utilities.getstream_Handle();
		baos=this.writetobytearray(baos);
        //baos.write(HANDSHAKE_MSG_HEADER.getBytes());
        //baos.write(new byte[10]);  
        this.peerID = peerid;
        baos.write(Utilities.getBytes(peerID));
        this.FullMessage = baos.toByteArray();
        Utilities.returnstream_Handle();
    }
	
	
	public HandshakeMessage(byte[] HandShakeMsg)
	{
		this.FullMessage = HandShakeMsg;
		this.peerID  = Utilities.getInteger_From_Byte(FullMessage, 28);
	}

    public ByteArrayOutputStream writetobytearray(ByteArrayOutputStream baos) throws InterruptedException, IOException
    {
    	baos.write(HANDSHAKE_MSG_HEADER.getBytes());
        baos.write(new byte[10]); 
		//baos.write(payloadBitmap);
        return baos;

    }


}
