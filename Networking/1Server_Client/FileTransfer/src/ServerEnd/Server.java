package ServerEnd;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket=new ServerSocket(5000);
        while (true) {
            Socket clientSocket=null;
            try {
                System.out.println("Server is starting in port 5000......");


                clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress().toString() + " is connected");


                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());



                System.out.println("Assigning new thread for the client "+
                        clientSocket.getInetAddress().toString());

                String path = ("/home/student/LLLAB3.pdf");

                Thread t = new FileTransferClientHandler(clientSocket, dataInputStream, dataOutputStream);
                t.start();
            } catch (Exception e) {
                clientSocket.close();
                System.out.println(e.getMessage());
            }
        }
    }

}

class FileTransferClientHandler extends Thread{
    final DataOutputStream dataOutputStream;
    final DataInputStream dataInputStream;

    final Socket socket;
    String path;

    private static DataInputStream dataInputStream1=null;
    private static DataOutputStream dataOutputStream1=null;

    public FileTransferClientHandler(Socket s,DataInputStream dataInputStream,
                                     DataOutputStream dataOutputStream){
        this.socket=s;
        this.dataInputStream=dataInputStream;
        this.dataOutputStream=dataOutputStream;
    }

    @Override
    public void run() {
        String received;
        String toServer;

        while(true){
            try{
                //Asking which file which client wants
                dataOutputStream.writeUTF("Type the file name which you want or to " +
                        "exit please type 'Exit':");
                received=dataInputStream.readUTF();

                System.out.println("client "+socket.getInetAddress().toString()+" requested a file: "+received);

                dataInputStream1=new DataInputStream(socket.getInputStream());
                dataOutputStream1=new DataOutputStream(socket.getOutputStream());

                if(received.equals("Exit")||received.equals("Quit")||
                        received.equals("quit")||received.equals("exit")){
                    System.out.println("closing connection for the client: "+socket.getInetAddress().toString());
                    socket.close();
                    break;
                }
                else{
                    sendFile(received,socket.getInetAddress().toString(),dataOutputStream,dataInputStream);
                }

            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Closed");
        try{
            dataInputStream.close();
            dataOutputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    static void sendFile(String path, String ip, DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws Exception{
        try{
            int bytes=0;

            File file=new File(path);

            FileInputStream fileInputStream;
            fileInputStream=new FileInputStream(file);

            dataOutputStream1.writeLong(file.length());
            dataOutputStream.writeUTF("OK");
            byte[] buffer=new byte[4*1024];

            while((bytes=fileInputStream.read(buffer))!=-1){

                dataOutputStream1.write(buffer,0,bytes);
                dataOutputStream1.flush();
            }

            System.out.println("File("+path+")has been transferred to client: "+ip+"\n");
            fileInputStream.close();
        }catch (Exception e){
            dataOutputStream1.writeLong(1);
            dataOutputStream.writeUTF("Sorry");
            System.out.println(e.getMessage()+"Failed");
        }
    }
}