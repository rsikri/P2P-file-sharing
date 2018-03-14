import java.io.*;
import java.util.*;


public class Incomingconnection
{
 public String getDate1() {
       
       Date date = new Date();
        

       return (date.toString());
   }

public void serverincomingconnection(String peer1, String address, String port,String filename)
{
 try
           {
               FileWriter fw = new FileWriter(filename,true); 
               BufferedWriter bw = new BufferedWriter(fw);
               String str = getDate1()+"***Server for peer: " +peer1+ " is ready to listen at :" + address + " at port : " + port ;
               bw.write(str);
               bw.newLine();
               bw.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }

}

public void tcpoutgoingconnection(String peer1,String peer2,String filename)
{
	try
           {
             
               FileWriter fw = new FileWriter(filename,true); 
               BufferedWriter bw = new BufferedWriter(fw);
               String str = getDate1()+": Peer "+peer1+" makes a connection to Peer "+peer2+".";
               bw.write(str);
               bw.newLine();
               bw.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
}

public void tcpincoming(String peer1,String peer2,String filename)
{
try
           {
               FileWriter fw = new FileWriter(filename,true); 
               BufferedWriter bw = new BufferedWriter(fw);
               String str = getDate1()+": Peer "+peer1+" is connected from Peer "+peer2+".";
               bw.write(str);
               bw.newLine();
               bw.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }


}


}