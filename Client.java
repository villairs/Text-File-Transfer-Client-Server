import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

//this class serves as a base, 
public abstract class Client{
  
  Socket sock;
  DatagramSocket dSock;
  String hostname,localName,remoteName;
  int portnumTCP, portnumUDP;
  InetAddress address;
  BufferedReader in;
  PrintWriter out;
  
  public Client(String hostname, int portnumTCP, int portnumUDP, InetAddress address, String localName, String remoteName){
    
   this.hostname=hostname;
   this.portnumTCP=portnumTCP;
   this.portnumUDP=portnumUDP;
   this.localName=localName;
   this.remoteName=remoteName;
   this.address = address;
    try{ 
      //create datagram socket at next open port
      dSock = new DatagramSocket(0);
    }
    catch(Exception e){}
  }

  //START SIGNALING THE SERVER TO SEND OVER FILES LIST
  public void signal(){
    try{
  byte[] blankArray = new byte[256];
System.out.println("start signal");
  DatagramPacket signalPack = new DatagramPacket(blankArray,blankArray.length,address,portnumUDP);
  dSock.send(signalPack);
System.out.println("sent signal");}
    catch(Exception e){}
  }
    
  //GET FILES LIST FROM THE SERVER
  public void getFileList(){
    try{
  byte[] blankArray = new byte[256];
  DatagramPacket pack = new DatagramPacket(blankArray,blankArray.length);
  dSock.receive(pack);
  String temp = new String(pack.getData(), "UTF-8");
  int num = Integer.parseInt(temp);
  FileWriter fw = new FileWriter("recievedFileList.txt");
  
  for (int i = 0; i < num; i++){
  dSock.receive(pack);
  temp = new String(pack.getData(), "UTF-8");
  fw.write(temp);
  }
    }
    catch(Exception e){}
  }
  
  //makes the tcp connection
  public void connectTCP(){
    try{
  sock = new Socket(address,portnumTCP);
  out = new PrintWriter(sock.getOutputStream(), true);
  in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }
    catch(UnknownHostException e){
    System.out.println("dont know host " + hostname);
    }
    catch(IOException e){
     System.out.println("couldnt get io connection to " + hostname);
     
    }
  }
  
  //
  public abstract void commandTCP();
  
  
  

}