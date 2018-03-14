import java.io.IOException;
import java.util.*;

public class ManageNeighbours implements Runnable
{
	private Connection myConnection;
	private Set<Integer> preferredPeerIDSet;
	WriteLog w = new WriteLog();

	public ManageNeighbours(Connection myConnection)
	{
		this.myConnection = myConnection;
		this.preferredPeerIDSet= new TreeSet<Integer>();
	}

	public void run()
	{
		try
		{
			findNeighbours();
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

	
	private void findNeighbours() throws IOException, InterruptedException
	{
		List<double[]> peerDownloadRatesList = new ArrayList<double[]>();
		Set<Integer> interestedNeighborsList = myConnection.getmyInterestedNeighbours();
		for(Integer peerID : interestedNeighborsList)
		{
			double[] peerIDAndRatePair = new double[2];
			peerIDAndRatePair[0] = (double)peerID;
			peerIDAndRatePair[1] = myConnection.getDownloadRate(peerID);
			peerDownloadRatesList.add(peerIDAndRatePair);
		}
		Collections.sort(peerDownloadRatesList, new Comparator<double[]>()
				{
			@Override
			public int compare(double[] rate1, double[] rate2)
			{
				if(rate1[1] > rate2[1])
					return 1;
				else if(rate1[1] < rate2[1])
					return -1;
				else
					return 0;
			}
				});

		Set<Integer> SetPreferred = new TreeSet<Integer>();
		 int count=setcount(peerDownloadRatesList,myConnection);


				for(int i = 0; i < count; i++)
				{
					int peerID = (int)peerDownloadRatesList.get(i)[0];
					if(!preferredPeerIDSet.contains(peerID))
					{
						myConnection.reportUnchokedPeer(peerID);
					}
					SetPreferred.add(peerID);
				}

				for(Integer peerID : this.preferredPeerIDSet)
				{
					if(!SetPreferred.contains(peerID))
					{
						myConnection.reportChokedPeer(peerID);
					}
				}

				this.preferredPeerIDSet = SetPreferred;

				if(preferredPeerIDSet.size() > 0)
				{
					String makeList = "";
					for(Integer peerID : this.preferredPeerIDSet)
					{
						makeList += peerID;
						makeList += ",";
					}
					String makeString = makeList.substring(0, makeList.length()-1);
					w.PrefNeighbours(Integer.toString(myConnection.getMyPeerID()), makeString);
				}
	}

	public int setcount(List<double[]> peerDownloadRatesList ,Connection myConnection)
{


	int count = peerDownloadRatesList.size() < myConnection.NUM_PREFERRED_NEIGHBORS ?
				peerDownloadRatesList.size() : myConnection.NUM_PREFERRED_NEIGHBORS;
return count;
}
}


