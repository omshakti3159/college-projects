import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;

import java.sql.*;

public class BankingSystem {

    public static void main(String args[]) {
        int ch;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234567890");

            Registry reg = LocateRegistry.getRegistry("localhost", 3159);
            BankIntf bankintf = (BankIntf) reg.lookup("BankServer");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\nEnter the Account Number:");
            int accountnumber = Integer.parseInt(br.readLine());
            
            String checkExistsQry = "SELECT * FROM accounts WHERE account_number='" + accountnumber + "'";

            try ( Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(checkExistsQry);
                if (!rs.next()) {
                    
                    System.out.println("\nEnter the UserName:");
                    String username = br.readLine();

                    System.out.println("\nEnter the Initial Amount:");
                    int amount = Integer.parseInt(br.readLine());
                    
                    String insertQuery = "INSERT INTO accounts VALUES('" + Math.random() * 10000 + "','" + username + "','" + accountnumber + "','" + amount + "','" + LocalDateTime.now() + "')";
                    String transactionQuery = "INSERT INTO transactions VALUES('" + Math.random() * 100000 + "','" + accountnumber + "','" + 0 + "','" + amount + "','" + amount + "','" + LocalDateTime.now() + "')";
                    
                    
                    PreparedStatement prepsInsertAccount = con.prepareStatement(insertQuery);
                    PreparedStatement prepsInsertTransaction = con.prepareStatement(transactionQuery);
                    prepsInsertAccount.execute();
                    prepsInsertTransaction.execute();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }

            do {

//                s=rs.getString(2);
//                ac=Integer.parseInt(rs.getString(3));
//                amt=Integer.parseInt(rs.getString(4));
                System.out.println("\n\t1.Withdraw\n\t2.Deposit\n\t3.Balance\n\t4.Exit");
                System.out.println("\nEnter your choice:");
                ch = Integer.parseInt(br.readLine());

                String s;
                int ac, amt;

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from accounts where account_number='" + accountnumber + "'");
                while (rs.next()) {
                    s = rs.getString(2);
                    ac = Integer.parseInt(rs.getString(3));
                    amt = Integer.parseInt(rs.getString(4));
                    switch (ch) {
                        case 1 -> {
                            System.out.println("\nEnter amount of Withdraw:");
                            int wd = Integer.parseInt(br.readLine());
                            System.out.println("\nUserName:" + s);
                            System.out.println("Account Number:" + ac);
                            if (wd > amt) {
                                System.out.println("Balance less unable to proceed withdraw");
                            } else {
                                amt = bankintf.withdraw(wd, amt, ac);
                                System.out.println("Balance:" + amt);
                            }
                        }
                        case 2 -> {
                            System.out.println("\nEnter amount of deposit:");
                            int dp = Integer.parseInt(br.readLine());
                            System.out.println("\nUserName:" + s);
                            System.out.println("Account Number:" + ac);
                            amt = bankintf.deposit(dp, amt, ac);
                            System.out.println("Balance:" + amt);
                        }
                        case 3 -> {
                            System.out.println("\nUserName:" + s);
                            System.out.println("Account Number:" + ac);
                            amt = bankintf.balance(ac);
                            System.out.println("Balance:" + amt);
                        }
                    }
                }
            } while (ch < 4);

        } catch (IOException | NumberFormatException | NotBoundException | SQLException e) {
            System.out.println("Exception :" + e);
        }
    }
}
