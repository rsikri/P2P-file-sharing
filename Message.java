import java.io.Serializable;

public class Message implements Serializable 
{
	protected static final long serialVersionUID = 1L;

	int MsgTypeValue;
	String MsgType;
	byte[] FullMessage;
	int MsgLength;
	
	public void setMessage_Type(String msg)
	{
		this.MsgType = msg;
	}

    public void setMessage_Type_Value(int msgVal)
	{
		this.MsgTypeValue = msgVal;
	}

    public int getMessage_Type_Value()
	{
		return MsgTypeValue;
	}

    public byte[] getFullMessage()
	{
		return FullMessage;
	}

	public String getMessage_Type()
	{
		return MsgType;
	}

	public void setMessage_Length(int msglen)
	{
		this.MsgLength = msglen;
	}

	public int getMessage_Length()
	{
		return MsgLength;
	}

	public void setFullMessage(byte[] pack)
	{
		this.FullMessage = pack;
	}

	
	
}
