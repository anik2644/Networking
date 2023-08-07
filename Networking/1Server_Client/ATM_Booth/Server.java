//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 {
    String user;
    String password;
    int balance;
    int req_id;

    Server2(String var1, String var2, int var3) {
        this.user = var1;
        this.password = var2;
        this.balance = var3;
    }

    public void setBalance(int var1) {
        this.balance = var1;
    }

    public int getBalance() {
        return this.balance;
    }

    public int getReq_id() {
        return this.req_id;
    }

    public void setReq_id() {
        this.req_id = this.getReq_id() + 1;
    }

    public String checkBalance() {
        return "Your current balance is: " + this.getBalance() + "taka";
    }

    public void credit(int var1) {
        this.setBalance(this.getBalance() + var1);
    }

    public boolean debit(int var1) {
        if (this.getBalance() >= var1) {
            this.setBalance(this.getBalance() - var1);
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] var0) throws IOException {
        int var1 = -1;
        Server2[] var2 = new Server2[]{new Server2("a", "a", 50000), new Server2("Karim", "1234", 60000), new Server2("rafiq", "1234", 40000)};
        System.out.println("Server started");
        System.out.println("Waiting for Clients...");
        ServerSocket var3 = new ServerSocket(5000);
        Socket var4 = var3.accept();
        System.out.println("Client Accepted");
        ObjectInputStream var5 = new ObjectInputStream(var4.getInputStream());
        ObjectOutputStream var6 = new ObjectOutputStream(var4.getOutputStream());

        try {
            Object var7 = var5.readObject();
            Object var8 = var5.readObject();
            String var9 = (String)var7;
            String var10 = (String)var8;

            for(int var11 = 0; var11 < 3; ++var11) {
                if (var9.equals(var2[var11].user) && var10.equals(var2[var11].password)) {
                    var6.writeObject(true);
                    var1 = var11;
                    break;
                }

                var6.writeObject(false);
            }

            while(true) {
                String var12;
                do {
                    Object var16 = var5.readObject();
                    var12 = (String)var16;
                } while(var1 < 0);

                Object var13;
                int var14;
                if (var12.equals("c")) {
                    var6.writeObject("Enter amount to be credited\n");
                    var13 = var5.readObject();
                    var14 = (Integer)var13;
                    var2[var1].credit(var14);
                    var6.writeObject("Your account has been credited by " + var14 + " taka\n" + var2[var1].checkBalance());
                } else if (var12.equals("d")) {
                    var6.writeObject("Enter amount to be debited\n");
                    var13 = var5.readObject();
                    var14 = (Integer)var13;
                    if (var2[var1].debit(var14)) {
                        var6.writeObject("Your account has been debited by " + var14 + " taka\n" + var2[var1].checkBalance());
                    }
                } else if (var12.equals("q")) {
                    var6.writeObject("Log Out Successful");
                } else if (var12.equals("b")) {
                    var6.writeObject(var2[var1].checkBalance());
                }
            }
        } catch (Exception var15) {
            var15.printStackTrace();
        }
    }
}
