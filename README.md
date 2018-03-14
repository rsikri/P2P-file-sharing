# P2P-file-sharing
A Java P2P file sharing software similar to BitTorrent.

The  protocol  consists  of  a  handshake  followed by  a  never-ending  stream  of  length-prefixed messages. Whenever  a  connection  is  established  between  two  peers,  each  of  the  peers  of  the connection  sends  to  the  other  one  the  handshake  message  before  sending  other messages.

The  handshake message consists  of  three  parts:  handshake  header,  zero  bits,  and  peer  ID. 
After handshaking, each peer can send a stream of actual messages. An actual message consists  of  4-byte  message  length  field,  1-byte  message  type  field,  and  a  message payload with variable size.‘choke’, ‘unchoke’, ‘interested’ and ‘not interested’messages have no payload'
‘have’ messages have a payload that contains a 4-byte piece index field. 
‘bitfield’ messages have a bitfield as its payload.
‘request’ messages  have  a  payload  which  consists  of  a  4-byte  piece  index  field.
‘piece’messages  have  a  payload  which  consists  of  a  4-byte  pieceindex  field  and  the 

In  the  project,  there  are  two  configuration  files  which the peer  process  should  read. The  common  properties  used  by  all  peers  are  specified  in  the  file Common.cfg as follows: NumberOfPreferredNeighbors, UnchokingInterval, ptimisticUnchokingInterval, FileName, FileSize, PieceSize
The other file is PeerInfo.cfg.
