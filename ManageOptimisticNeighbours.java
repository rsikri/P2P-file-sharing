import java.util.*;
import java.io.IOException;


public class ManageOptimisticNeighbours implements Runnable
{
	WriteLog w = new WriteLog();
	private Connection myConnection;
	

	public ManageOptimisticNeighbours(Connection Connection)
	{
		this.myConnection = Connection;
	}

	
	
	private void optUnchokedPeer() throws IOException, InterruptedException
	{

		Integer prevPeer = myConnection.getUnchokedPeer_Prev();
		if (prevPeer != -1)
			myConnection.reportChokedPeer(myConnection.getUnchokedPeer_Prev());
         writeopt(prevPeer);

    }

    @Override
	public void run()
	{
		try
		{
			this.optUnchokedPeer();
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
	}


    public void writeopt(Integer prevPeer) throws IOException, InterruptedException
    {
		Set<Integer> chokedPeersSet = myConnection.getChokedPeers();
		List<Integer> interestedAndChoked = new LinkedList<Integer>();
		interestedAndChoked.addAll(myConnection.getmyInterestedNeighbours());
		interestedAndChoked.retainAll(chokedPeersSet);
		if(interestedAndChoked.size() > 0)
		{
			Random rand = new Random();
			int selectedPeer = interestedAndChoked.get(rand.nextInt(interestedAndChoked.size()));
			myConnection.reportUnchokedPeer(selectedPeer);
			myConnection.setUnchokedPeer_Prev(selectedPeer);
			w.OptUnchokedNeighbours(Integer.toString(myConnection.getMyPeerID()), Integer.toString(selectedPeer));
		}
	}
}
