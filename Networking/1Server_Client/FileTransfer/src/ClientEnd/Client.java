package ClientEnd;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            Scanner scn = new Scanner(System.in);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos= new DataOutputStream(socket.getOutputStream());

            System.out.println("Receiving the File from the Server");

            // Call SendFile Method
            while (true) {
                System.out.printf(dis.readUTF());
                String toserver = scn.nextLine();
                dos.writeUTF(toserver);

                dataInputStream=new DataInputStream(socket.getInputStream());
                dataOutputStream=new DataOutputStream(socket.getOutputStream());

                if (toserver.equals("Exit") || toserver.equals("Quit") ||
                        toserver.equals("quit") || toserver.equals("exit")) {
                    System.out.printf("Connection to server is closing");
                    socket.close();
                    break;
                }

                receiveFile(toserver,socket);
            }
            scn.close();
            dis.close();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(String s,Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos= new DataOutputStream(socket.getOutputStream());
        try{
            int bytes = 0;

            FileOutputStream fileOutputStream = new FileOutputStream(s);

            long size = dataInputStream.readLong();

            String status=dis.readUTF();
            System.out.println(status);
            if(status.equals("OK")){
                byte[] buffer = new byte[4 * 1024];

                while (size > 0 && (bytes = dataInputStream.read(buffer, 0,
                        (int) Math.min(buffer.length, size))) != -1) {
                    fileOutputStream.write(buffer, 0, bytes);
                    size -= bytes;
                }
                System.out.println("File has been received");
                fileOutputStream.close();
            }
            else{
                System.out.println("No such file");
            }
        }catch (Exception e){
            System.out.printf(e.getLocalizedMessage());
        }
    }
}