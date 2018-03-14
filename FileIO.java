import java.io.*;



public class FileIO
{
    private File myFile;
    private int myFileSize;    
    private int myPieceSize;
    private final int SIZEOFEACHCHUNK = 1 << 26; 

    public FileIO(String PathofFile, int SizeofFile, int SizeofPiece) throws FileNotFoundException
    {
        this.myFile = new File(PathofFile);
        this.myFileSize = SizeofFile;
        this.myPieceSize = SizeofPiece;
    }
   
   
    public boolean createDummyFile() throws IOException
    {
        if(myFile.exists())
        {
            if(!myFile.delete())
            {
                return false;
            }
        }
        if(!myFile.createNewFile())
        {
            return false;
        }
       
        FileOutputStream fos = new FileOutputStream(myFile);
        byte[] tempBuffer = new byte[SIZEOFEACHCHUNK];
        for(int i = SIZEOFEACHCHUNK; i < myFileSize; i += SIZEOFEACHCHUNK)
        {
            fos.write(tempBuffer);
        }
        int remainingBytes = myFileSize & (SIZEOFEACHCHUNK - 1);    
        tempBuffer = new byte[remainingBytes];
        fos.write(tempBuffer);
       
        fos.close();
        return true;
    }
   
    
    public byte[] getChunk(int pieceID) throws IOException
    {
        RandomAccessFile ran = new RandomAccessFile(myFile, "r");
        ran.seek(Math.max(pieceID*myPieceSize, 0));
       
        byte[] buffer;
        long remainingBytes = ran.length() - (pieceID*myPieceSize);
        if (remainingBytes < myPieceSize)
                buffer = new byte[(int)remainingBytes];
        else
                buffer = new byte[myPieceSize];
        ran.read(buffer);
        ran.close();
        return buffer;
    }

    public static boolean createDirectory(String someDirectoryPath)
    {
        File directory = new File(someDirectoryPath);
        if(directory.exists())
        {
            return true;
        }
        return directory.mkdir();
    }

    public void writeFilePiece(int pieceID, byte[] pieceData) throws IOException
    {
        RandomAccessFile ran = new RandomAccessFile(this.myFile, "rw");
        ran.seek(Math.max(pieceID *myPieceSize, 0));
        ran.write(pieceData);
        ran.close();
    }    
}

