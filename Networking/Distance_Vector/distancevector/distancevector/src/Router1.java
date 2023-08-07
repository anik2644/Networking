
import java.io.*;
import java.net.*;
import java.util.*;

public class Router1 {


    public static void arrayPrint(int[][] arr) {
        int asciiValue = 65;
        for (int i = 0; i < 3; i++)
        {


            char character = (char) (asciiValue+i);
            char ch = (char) (asciiValue+ arr[1][i]);
            //(char) asciiValue
            System.out.println("from A to " + character +
                    " cost is "+ arr[0][i]+ " output port "+ ch);
        }


        System.out.println();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        System.out.println("\nRouter 1 is active now\n");
        DatagramSocket socket = new DatagramSocket(7000);

        while (true) {

            byte[] buffer =   new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
            ObjectInputStream in = new ObjectInputStream(bais);
            int[][] arr = (int[][]) in.readObject();

            arrayPrint(arr);
//            for(int i= 0;i<3;i++)
//            {
//                System.out.println(array[i]);
//            }


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            socket.receive(packet);
            String string = new String(packet.getData(), 0, packet.getLength());
            int n = Integer.parseInt(string);

           // System.out.println(number);

            if (n == 1)
                System.out.println("\n1 -> Router 2\n");


        }
    }
}