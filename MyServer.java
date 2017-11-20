import java.net.*;
import java.io.*;

public class MyServer extends Thread{
  ServerSocket serverSock;
  DatagramSocket dSock;
  PrintWriter out;
  BufferedReader in;
  BufferedReader fileReader;
  Socket client;
  boolean cont;
  
// CONSTRUCTOR
// USES TCP PORT 65078
// USES UDP PORT 57116
  public MyServer(){
    try{
      cont= true;
      serverSock = new ServerSocket(0);
      dSock = new DatagramSocket(0);
      
      //PRINT OUT INFORMATION NEEDED TO MAKE THE CONNECTION
      System.out.println("Host Name / IP address is " + InetAddress.getLocalHost());
      System.out.println("TCP port is " + serverSock.getLocalPort());
      System.out.println("UDP port is " + dSock.getLocalPort());
      
    }
    catch(Exception e){}
    
  }
  
  
  //CREATES A FILE CONTAINING THE NAMES OF ALL THE FILES WITHIN THE FOLDER
  public void updateFiles(){
    String currentDir = System.getProperty("user.dir");
    File curFolder = new File(currentDir);
    File[] listOfFiles = curFolder.listFiles();
    try{
      PrintWriter fw = new PrintWriter("fileList.txt");
      for(int i = 0; i < listOfFiles.length;i++){
        fw.println(listOfFiles[i].getName());}
      fw.close();}
    catch(Exception e){}
  }
  
  
  //SENDS THE LIST OF FILES TO THE CLIENT OVER UDP
  public void sendFileList(){
    System.out.println("starting to send files list");
    try{
      byte[] b = new byte[256];
      byte[] sendData = new byte[256];
      DatagramPacket packet = new DatagramPacket(b,b.length);
      
      //RECIEVE EMPTY PACKET FROM CLIENT TO GET INFORMATION ABOUT DESTINATION
      System.out.println("waiting on signal packet");
      dSock.receive(packet);
      System.out.println("received signal packet");
      InetAddress destAddr = packet.getAddress();
      int destPort = packet.getPort();
      BufferedReader fInput = new BufferedReader(new InputStreamReader( new FileInputStream("fileList.txt")));
      fInput.mark(0);
      int count = 0;
      
      //FIRST PACKET SENT CONTAINS INFORMATION ABOUT HOW MANY PACKETS WILL BE SENT
      while(fInput.readLine() != null){
        count++;
      }
      
      fInput.close();
      fInput = new BufferedReader( new FileReader("fileList.txt"));
      
      sendData = (""+count).getBytes("UTF-8");
      DatagramPacket sendP = new DatagramPacket(sendData,sendData.length,destAddr,destPort);
      dSock.send(sendP);
      
      String line = fInput.readLine();
      //SEND THE REST OF THE PACKETS
      while(line != null){
        sendData = line.getBytes("UTF-8");
        sendP = new DatagramPacket(sendData,sendData.length,destAddr,destPort);
        dSock.send(sendP);
        line = fInput.readLine();
      }
      fInput.close();
    }
    
    catch(UnknownHostException e){
      System.out.println("problem with host name or port nums");}
    catch (IOException e){
      String curDir = System.getProperty("user.dir");
      System.out.println(curDir);
      System.out.println("problem with io");
      System.out.println(e);
    }
    
  }
  
  
  //ACCEPT THE TCP CONNECTION FROM THE CLIENT
  public void acceptTCPconnection(){
    try{
      client = serverSock.accept();
      out = new PrintWriter(client.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      System.out.println("TCP connection accepted");
      //AFTER ACCEPTING THE CONNECTION, HANDLE THE COMMAND THAT FOLLOWS
      handleCommand();
    }
    catch(UnknownHostException e){
      System.out.println("error accepting connection");}
    catch(IOException e){
      System.out.println("error with io while accepting TCP connection");}
  }
  
  //HANDLE THE SIGNAL FROM THE CLIENT, SEND LIST OF FILES THEN WAIT FOR TCP CONNECTION
  public void handleSignal(){
    updateFiles();
    sendFileList();
    acceptTCPconnection();
  }
  
  
  //HANDLES GET AND PUT COMMANDS
  public void handleCommand(){
    try{
      System.out.println("handling commands");
      String temp1 = in.readLine();
      System.out.println(temp1);
      String[] parts = temp1.split(" ");
      if(parts.length == 1){
        parts = temp1.split("\t");}
      if(parts[0].equals("put")){
        putCommand(parts[parts.length-1]);
      }
      else if(parts[0].equals("get")){
        getCommand(parts[parts.length-1]);
      }
      out.close();
      in.close();
      client.close();
    }
    catch(Exception e){
      System.out.println("error");}
  }
  
  //PUT FILE COMMAND
  //MAKES A FILE THEN READS INPUT FROM SOCKET, WRITES INTO FILE
  public void putCommand(String s){
    try{
      PrintWriter fw = new PrintWriter(new FileWriter(s));
      if(fw == null)
        out.println("error failed to create file");
      else
        out.println("ok");
      String temp = in.readLine(); 
      while (temp != null){
        fw.println(temp);
        temp = in.readLine();
      }
      fw.close();
    }
    catch(IOException e){
      out.write("error failed");
    }
    
  }
  
  //GET FILE COMMAND
  //READS INPUT FROM FILE, SENDS INTO SOCKET
  public void getCommand(String s){
    try{
      BufferedReader fw = new BufferedReader(new FileReader(s));
      if(fw == null)
        out.println("error failed to open file");
      else
        out.println("ok");
      String temp = fw.readLine(); 
      while (temp != null){
        out.println(temp);
        temp = fw.readLine();
      }
      fw.close();
    }
    catch(IOException e){
      out.write("error " + e);
    }
    
  }
  
  
  
  
  
  //MAIN METHOD, WHILE IT RUNS, KEEP ACCEPTING SIGNALS AND STUFF
  public static void main(String[] args) throws IOException{
    MyServer m = new MyServer();
    while(true){
      m.handleSignal();}
  }
  
}
