import java.io.*;


public class HaveMessage extends Message {
	private static final long serialVersionUID = 9L;
	
	
	public HaveMessage(int pieceIndex) throws InterruptedException, IOException
	{
		this.MsgType = "HaveMessage";
		this.MsgTypeValue = 4;
		this.MsgLength = (Utilities.getBytes(pieceIndex)).length+1;
		
		
		
		ByteArrayOutputStream baos = Utilities.getstream_Handle();
        
        baos=this.writetobytearray(baos, pieceIndex);

		/*baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		baos.write(Utilities.getBytes(pieceIndex));*/
		FullMessage = baos.toByteArray();
		Utilities.returnstream_Handle();
	}
    
    public ByteArrayOutputStream writetobytearray(ByteArrayOutputStream baos, int pieceIndex) throws InterruptedException, IOException
    {
    	baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		baos.write(Utilities.getBytes(pieceIndex));
        return baos;

    }

}
