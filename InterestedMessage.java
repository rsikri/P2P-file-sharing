import java.io.*;


public class InterestedMessage extends Message 
{
	private static final long serialVersionUID = 7L;

	
	public InterestedMessage() throws InterruptedException, IOException
	{	
	    this.MsgLength = 1;	
	    this.MsgTypeValue = 2;
		this.MsgType = "InterestedMessage";
		
		

		ByteArrayOutputStream baos = Utilities.getstream_Handle();
		baos=this.writetobytearray(baos);
		//baos.write(Utilities.getBytes(this.MsgLength));
		//baos.write((byte)this.MsgTypeValue);
		FullMessage = baos.toByteArray();
		Utilities.returnstream_Handle();
	}

	public ByteArrayOutputStream writetobytearray(ByteArrayOutputStream baos) throws InterruptedException, IOException
    {
    	baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		//baos.write(payloadBitmap);
        return baos;

    }
}
