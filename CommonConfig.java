public class CommonConfig
{
    private int unchokingInterval;
    private int numPreferredNeighbours;
    private String fileName;
    private int optimisticUnchokingInterval;
    private int pieceSize;

    private int fileSize;
    
    public CommonConfig(int numPreferredNeighbours, int unchokingInterval,
            int optimisticUnchokingInterval, String fileName, int fileSize,
            int pieceSize)
    {
        this.numPreferredNeighbours = numPreferredNeighbours;
        this.unchokingInterval = unchokingInterval;
        this.optimisticUnchokingInterval = optimisticUnchokingInterval;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.pieceSize = pieceSize;
    }

    public CommonConfig()
    {
    } 

   protected void setUnchokingInterval(int unchokingInterval)
    {
        this.unchokingInterval = unchokingInterval;
    }

    protected void setNumPreferredNeighbours(int numPreferredNeighbours)
    {
        this.numPreferredNeighbours = numPreferredNeighbours;
    }

    

    protected void setOptimisticUnchokingInterval(
            int optimisticUnchokingInterval)
    {
        this.optimisticUnchokingInterval = optimisticUnchokingInterval;
    }

    

    protected void setFileSize(int fileSize)
    {
        this.fileSize = fileSize;
    }

    public int getUnchokingInterval()
    {
        return unchokingInterval;
    }
   
    protected void setPieceSize(int pieceSize)
    {
        this.pieceSize = pieceSize;
    }

   protected void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public int getNumPreferredNeighbours()
    {
        return numPreferredNeighbours;
    }

    

    public int getOptimisticUnchokingInterval()
    {
        return optimisticUnchokingInterval;
    }
   
   public int getPieceSize()
    {
        return pieceSize;
    }


    public String getFileName()
    {
        return fileName;
    }

    public int getFileSize()
    {
        return fileSize;
    }

    
}

