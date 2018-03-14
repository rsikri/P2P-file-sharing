import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Connection
{
	private int myListenerPort;
	private final int pieceSize;
	private AtomicInteger UnchokedPeer_Prev;
	private ConcurrentHashMap<Integer, ClientConnection> peerConnectionMap;
	private Map<Integer, Double> downloadrate_peer; // peer id --> download rate
	private Set<Integer> setofChokedPeers = new ConcurrentSkipListSet<Integer>();
	private CommonConfig myCommonConfig;
	WriteLog w = new WriteLog();
	final int NUM_PREFERRED_NEIGHBORS;
	private final int UNCHOKING_INTERVAL;
	private BitMap myBitMap;
	private Set<Integer> myInterestedNeighbours;
	private List<Integer> connectedPeersList;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final int OPTIMISTIC_UNCHOKING_INTERVAL;
	private Map<Integer, PeerConfig> peerInfoMap;
	private int myPeerID;
	private String myHostName;


	
	public Connection(CommonConfig myCommonConfig, Map<Integer, PeerConfig> peerMap, int myPeerID) throws IOException, InterruptedException
	{

		this.myCommonConfig = myCommonConfig;
		PeerConfig myConfig = peerMap.get(myPeerID);
		this.myHostName = myConfig.getHostName();
		this.myListenerPort = myConfig.getListeningPort();
		System.out.println("My host name = " + myHostName + " and my listening port is " + myListenerPort);

		this.pieceSize = myCommonConfig.getPieceSize();
		this.peerInfoMap = peerMap;
		this.myPeerID = myPeerID;
		this.downloadrate_peer = new HashMap<Integer, Double>();
		this.myInterestedNeighbours = new ConcurrentSkipListSet<Integer>();
		connectedPeersList = new ArrayList<Integer>();
		this.NUM_PREFERRED_NEIGHBORS = myCommonConfig.getNumPreferredNeighbours();
		this.UNCHOKING_INTERVAL = myCommonConfig.getUnchokingInterval();
		this.OPTIMISTIC_UNCHOKING_INTERVAL = myCommonConfig.getOptimisticUnchokingInterval();
		this.peerConnectionMap = new ConcurrentHashMap<Integer, ClientConnection>();
		connectedPeersList.addAll(this.peerInfoMap.keySet());
		UnchokedPeer_Prev = new AtomicInteger(-1);

		
		this.begin();
	}

	public void begin() throws SocketTimeoutException, IOException, InterruptedException
	{

		Thread serverThread = new Thread(new ServerConnection(myHostName, myListenerPort, this));
		serverThread.start();
		findConnectionStatus();
		this.myBitMap = new BitMap(myPeerID, myCommonConfig, peerInfoMap.keySet(), this.peerInfoMap.get(myPeerID).hasCompleteFile(), this,this.peerInfoMap);

		this.processPeerInfoMap();

		scheduler.scheduleAtFixedRate(new ManageNeighbours(this), 0, UNCHOKING_INTERVAL, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(new ManageOptimisticNeighbours(this), 0, OPTIMISTIC_UNCHOKING_INTERVAL, TimeUnit.SECONDS);
	}

	
	private void processPeerInfoMap() throws SocketTimeoutException, IOException
	{
		for(Integer a : this.peerInfoMap.keySet())
		{
			if(a < myPeerID)
			{
				System.out.println("Creating a client for " + myPeerID);

				ClientConnection newClient = new ClientConnection(this.peerInfoMap.get(a).getHostName(),
						this.peerInfoMap.get(a).getListeningPort(), this);
				MessageHandler aMessageHandler = new MessageHandler(newClient, this);
				(new Thread(aMessageHandler)).start();
				findConnectionStatus();
				this.peerConnectionMap.put(a, newClient);
			}
		}
	}


	public void sendGroupMessage(List<Integer> peerIDList, byte[] data) throws IOException
	{
		for (Integer a : peerIDList)
		{
			if(this.peerConnectionMap.containsKey(a))
			{
				this.peerConnectionMap.get(a).send(data);
			}
		}
	}

	protected void checkAvailability(){
		
		int i;

	}
	
	public void sendPeerMessage(int peerID, byte[] data) throws IOException
	{
		if(this.peerConnectionMap.containsKey(peerID))
		{
			this.peerConnectionMap.get(peerID).send(data);
		}

	}

	public BitMap getBitMap()
	{
		return this.myBitMap;
	}

	public int getMyPeerID()
	{
		return this.myPeerID;
	}

	public void reportInterestedPeer(int peerID)
	{
		this.myInterestedNeighbours.add(new Integer(peerID));
	}

	public void reportNotInterestedPeer(int peerID)
	{
		this.myInterestedNeighbours.remove(new Integer(peerID));
	}
	
	public void findConnectionStatus(){
		checkAvailability();
	}
	

	
	public List<Integer> getconnectedPeersList()
	{
		return connectedPeersList;
	}

	public List<Integer> computeAndGetWastePeersList()
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

	public synchronized void addOrUpdatedownloadrate_peer(Integer peerId, long elapsedTime)
	{              
		double downloadRate = (double)this.pieceSize/elapsedTime;
		checkAvailability();
		downloadrate_peer.put(peerId, downloadRate);
	}

	public synchronized void resetdownloadrate_peer(Integer peerId)
	{          
		downloadrate_peer.put(peerId, 0.0);
	}

	public double getDownloadRate(Integer peerId)
	{
		if(this.downloadrate_peer.containsKey(peerId))
			return downloadrate_peer.get(peerId);
		else
			return -1;
	}

	public Set<Integer> getmyInterestedNeighbours()
	{
		return this.myInterestedNeighbours;
	}

	
	public void reportChokedPeer(Integer peerID) throws IOException, InterruptedException
	{
		this.sendPeerMessage(peerID, new ChokeMessage().getFullMessage());
		this.setofChokedPeers.add(peerID);
	}

	
	public void reportUnchokedPeer(int peerID) throws IOException, InterruptedException
	{
		this.sendPeerMessage(peerID, new UnchokedMessage().getFullMessage());
		this.setofChokedPeers.remove(peerID);
	}

	public Set<Integer> getChokedPeers()
	{
		return this.setofChokedPeers;
	}

	public int getUnchokedPeer_Prev()
	{
		return UnchokedPeer_Prev.get();
	}

	public void setUnchokedPeer_Prev(int peerId)
	{
		this.UnchokedPeer_Prev.set(peerId);
	}

	public void reportNewClientConnection(int clientID, ClientConnection aClient)
	{
		this.peerConnectionMap.put(clientID, aClient);
	}

	public void QuitProcess()
	{
		
		
	      try{
		
		Thread.yield();
 		}
		catch(Exception e){
			
		}	
              scheduler.shutdown();
	}
}

