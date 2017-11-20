import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;
public class GetClient extends Client{
  
  
  public GetClient(String hostname, int portnumTCP, int portnumUDP, InetAddress address, String localName, String remoteName){
  
    super(hostname,portnumTCP,portnumUDP,address,localName,remoteName);
   
  }


  //gets a text file from the server
  
  public void commandTCP(){
    try{
      out.println("get " + remoteName);
      System.out.println("sent get command");
      System.out.println(in.readLine());
      PrintWriter fr = new PrintWriter(new FileWriter(localName));
      String temp = in.readLine();
 
      while(temp != null){
        fr.println(temp);
        temp = in.readLine();
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
  
  
  //main method
  //takes 5 arguments, remote-fileName, local-fileName, hostname,TCPport, UDPport
  public static void main(String[] args) throws IOException{
    String name = args[2];
    InetAddress ad = InetAddress.getByName(name);
    int portT = Integer.parseInt(args[3]);
    int portU = Integer.parseInt(args[4]);
    GetClient p = new GetClient(name,portT,portU,ad,args[1],args[0]);
    p.signal();
    p.getFileList();
    p.connectTCP();
    p.commandTCP();
  }

}
