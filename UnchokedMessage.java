import java.io.*;

public class UnchokedMessage extends Message {

	private static final long serialVersionUID = 6L;
	
	public UnchokedMessage() throws InterruptedException, IOException
	{		
		this.MsgType = "UnchokedMessage";
		this.MsgLength = 1;
		this.MsgTypeValue = 1;
		

		ByteArrayOutputStream baos = Utilities.getstream_Handle();
		baos=this.writetobytearray(baos);
		/*baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);*/
		FullMessage = baos.toByteArray();
		Utilities.returnstream_Handle();
	}
		
    public ByteArrayOutputStream writetobytearray(ByteArrayOutputStream baos) throws InterruptedException, IOException
    {
    	baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		//baos.write(Utilities.getBytes(pieceIndex));
        return baos;

    }
}
