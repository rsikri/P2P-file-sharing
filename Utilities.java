import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;




public class Utilities {

	
	private static ReentrantLock lock = new ReentrantLock();
	private static ByteArrayOutputStream stream_Handle = new ByteArrayOutputStream();
	
	private static boolean isStream_InUse = false;
    private static Condition borrow_stream = lock.newCondition();

    
	public static byte[] int_to_bytes( int i ) {
		ByteBuffer byte_Buff = ByteBuffer.allocate(4); 
		byte_Buff.putInt(i); 
		return byte_Buff.array();
	}

	
	public static int byteArray_to_Int(byte[] b) 
	{
		return   b[3] & 0xFF |
				(b[2] & 0xFF) << 8 |
				(b[1] & 0xFF) << 16 |
				(b[0] & 0xFF) << 24;
	}

    public static byte[] getBytes(int i)
	{
		byte[] res = new byte[4];
		res[0] = (byte) (0xFF & (i >> 24));
		res[1] = (byte) (i >> 16);
		res[2] = (byte) (i >> 8);
		res[3] = (byte) (i /*>> 0*/);

		return res;
	}

    public static synchronized ByteArrayOutputStream getstream_Handle() throws InterruptedException
	{
		lock.lock();
		try
		{
			if(isStream_InUse)
			{
				borrow_stream.await();
			}
			isStream_InUse = true;
			stream_Handle.reset();
			return stream_Handle;
		}
		finally
		{
			lock.unlock();
		}
	}

	public static int getInteger_From_Byte(byte[] array, int index){
		ByteBuffer buffer = ByteBuffer.wrap(array, index, 4);
		return buffer.getInt();
	}

	

	public static void returnstream_Handle()
	{
		lock.lock();
		try
		{
			borrow_stream.signal();
			isStream_InUse = false;
		}
		finally
		{
			lock.unlock();
		}
	}

}


