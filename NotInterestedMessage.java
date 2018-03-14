import java.io.*;


public class NotInterestedMessage extends Message 
{
private static final long serialVersionUID = 8L;
	
	public NotInterestedMessage() throws InterruptedException, IOException
	{		
		this.MsgType = "NotInterestedMessage";
		this.MsgLength = 1;
		this.MsgTypeValue = 3;
		

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
