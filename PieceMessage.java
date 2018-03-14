import java.io.*;


public class PieceMessage extends Message{
	private static final long serialVersionUID = 10L;

	public PieceMessage(int pieceIndex, byte[] data) throws IOException, InterruptedException
	{
		this.MsgTypeValue = 7;
		this.MsgType = "PieceMessage";
		
		this.MsgLength = data.length+4+1;

		ByteArrayOutputStream baos = Utilities.getstream_Handle();
		baos=this.writetobytearray(baos, pieceIndex, data);

		/*baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		baos.write(Utilities.getBytes(pieceIndex));
		baos.write(data);*/
		FullMessage = baos.toByteArray();
		Utilities.returnstream_Handle();
}

    public ByteArrayOutputStream writetobytearray(ByteArrayOutputStream baos, int pieceIndex, byte[] data) throws InterruptedException, IOException
    {
    	baos.write(Utilities.getBytes(this.MsgLength));
		baos.write((byte)this.MsgTypeValue);
		baos.write(Utilities.getBytes(pieceIndex));
		baos.write(data);
        return baos;

    }
}
