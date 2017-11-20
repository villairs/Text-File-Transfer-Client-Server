import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;

public class PutClient extends Client{
  
  
  public PutClient(String hostname, int portnumTCP, int portnumUDP, InetAddress address, String localName, String remoteName){
     super(hostname,portnumTCP,portnumUDP,address,localName,remoteName);
  }


  
  
  public void commandTCP(){
    try{
      
        
      out.println("put " + remoteName);
      System.out.println("sent put command");
      System.out.println(in.readLine());
      BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(localName)));
      String temp = fr.readLine();
      
      while(temp != null){
        out.println(temp);
        temp = fr.readLine();
      }
      
      out.close();
      in.close();
      sock.close();
      dSock.close();
      fr.close();
      System.exit(1);
    }
    catch(IOException e){
      
    }
  }
  
  
  
// takes 5 arguments, localFileName, remoteFileName, hostname, TCPport, UDPport
  public static void main(String[] args) throws IOException{
    String name = args[2];
    InetAddress ad = InetAddress.getByName(name);
    int portT = Integer.parseInt(args[3]);
    int portU = Integer.parseInt(args[4]);
    PutClient p = new PutClient(name,portT,portU,ad,args[0],args[1]);
    p.signal();
    p.getFileList();
    p.connectTCP();
    p.commandTCP();
  }

}
