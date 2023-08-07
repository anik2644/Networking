
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {

    static  int [] A= new int[3];
    static  int [] B= new int[3];
    static  int [] C= new int[3];
    private static final int MAX = Integer.MAX_VALUE;

    public static int getValue(int x, int y) {
        if ((x == 0 && y == 1) || (x == 1 && y == 0)) {
            return 2;
        } else if ((x == 0 && y == 2) || (x == 2 && y == 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean makeComparism(int[][][] A, int[][][] B, int i1, int j1, int i2, int j2) {
        //System.out.println("length is "+  A[0][0].length);
        for (int k = 0; k < A[0][0].length; k++) {
            if (A[i1][j1][k] != B[i2][j2][k]) {
                return false;
            }
        }
        return true;
    }


    public static void BellmanFord(int[][][] A, int[][] D, int x) {

        for (int y = 0; y < 3; y++) {

            int v = getValue(x, y);
            // System.out.println("hello "+  A[x][y][y]);
            A[x][x][y] = Math.min(D[x][y] + A[x][y][y], D[x][v] + A[x][v][y]);

          if(x==0)
          {
              if(D[x][y] + A[x][y][y]> D[x][v] + A[x][v][y])
              {
                  Main.A[y]= v;
              }
              else
              {
                  Main.A[y]= y;
              }
          }
           else if(x==1)
            {
                if(D[x][y] + A[x][y][y]> D[x][v] + A[x][v][y])
                {
                    Main.B[y]= v;
                }
                else
                {
                    Main.B[y]= y;
                }
            }
           else if(x==2)
            {
                if(D[x][y] + A[x][y][y]> D[x][v] + A[x][v][y])
                {
                    Main.C[y]= v;
                }
                else
                {
                    Main.C[y]= y;
                }
            }
        }
    }




    public static void main(String[] args) throws IOException {

        int[] port = new int[] { 7000, 8000, 9000 };

        int[][] D = {
                { 0, 2, 7 },
                { 2, 0, 1 },
                { 7, 1, 0 }

        };

        int[][][] Router = new int[3][3][3];

        Router[0] = new int[][] {
                { 0, 2, 7 },
                { MAX, MAX, MAX },
                { MAX, MAX, MAX }
        };
        Router[1] = new int[][] {
                { MAX, MAX, MAX },
                { 2, 0, 1 },
                { MAX, MAX, MAX }
        };
        Router[2] = new int[][] {
                { MAX, MAX, MAX },
                { MAX, MAX, MAX },
                { 7, 1, 0 }
        };

        int flag;
        while (true) {

            flag = 1;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i != j) {
                        if (makeComparism(Router, Router, j, i, i, i))
                        {
//                            System.out.print(i);
//                            System.out.print(" mhd ");
//                            System.out.println(j);
                            continue;
                        }

                        else {
                            System.arraycopy(Router[i][i], 0, Router[j][i], 0, 3);
//                            System.out.print(i);
//                            System.out.print(" anik ");
//                            System.out.println(j);
                            flag = 0;
                        }
                    }
                }
            }

            //System.out.println("oky chill");
            if (flag != 1) {
                for (int i = 0; i < 3; i++) {
                    BellmanFord(Router, D, i);
                }
            } else{
                break;
            }


        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {


                    int x = Router[0][i][j];

                    if (x == MAX)
                        System.out.print("∞ ");
                    else
                        System.out.print(Router[0][i][j] + " ");
            }
            System.out.println();
        }


        for(int i= 0;i<3;i++)
        {
            Router[0][1][i]=Main.A[i];
            Router[1][2][i]=Main.B[i];
            Router[2][0][i]=Main.C[i];

        }

        for(int i= 0;i<3;i++)
        {

            DatagramSocket socket1 = new DatagramSocket();
            InetAddress address1 = InetAddress.getByName("localhost");


            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream ot = new ObjectOutputStream(os);
            ot.writeObject(Router[i]);
            byte[] data1 = os.toByteArray();
            DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address1, port[i]);
            socket1.send(packet1);

            int j=i+1;
            System.out.println("information sent to router "+ j);

            byte[] data2 = String.valueOf(1).getBytes();
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address1, port[i]);
            socket1.send(packet2);

            System.out.println("2nd packet sent sent");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}