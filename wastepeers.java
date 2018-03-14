import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


class wastepeers
{
	public List<Integer> getwastepeer(List<Integer> connectedPeersList,BitMap myBitMap)
	{


		List<Integer> wastePeersList = new ArrayList<Integer>();
		for(Integer peerID : connectedPeersList)
		{
			if(!myBitMap.hasInterestingPiece(peerID))
			{
				wastePeersList.add(peerID);
			}
		}
		return wastePeersList;
	}

}