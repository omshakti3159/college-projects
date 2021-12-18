import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankImpl extends UnicastRemoteObject implements BankIntf {

    public BankImpl() throws RemoteException {
    }

    @Override
    public int withdraw(int a, int amt,int accno) throws RemoteException {
        
        try {
            amt = amt - a;
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234567890");
            String accUpdate="update accounts set amount='"+amt+"' where account_number='"+accno+"'";
            String trnsinsert="INSERT INTO transactions VALUES('" + Math.random() * 100000 + "','" + accno + "','" + a + "','" + 0 + "','" + amt + "','" + LocalDateTime.now() + "')";
            
            PreparedStatement prepupdateqry = con.prepareStatement(accUpdate);
            PreparedStatement inserttransc=con.prepareStatement(trnsinsert);
            
            prepupdateqry.execute();
            inserttransc.execute();
            
            con.close();
            return (amt);
        } catch (SQLException e) {
            System.out.println(e);
            return(0); 
        }
        
    }

    @Override
    public int deposit(int b, int amt,int accno) throws RemoteException {
        amt = amt + b;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234567890");
            String accUpdate="update accounts set amount='"+amt+"' where account_number='"+accno+"'";
            String trnsinsert="INSERT INTO transactions VALUES('" + Math.random() * 100000 + "','" + accno + "','" + 0 + "','" + b + "','" + amt + "','" + LocalDateTime.now() + "')";
            
            PreparedStatement prepupdateqry = con.prepareStatement(accUpdate);
            PreparedStatement inserttransc=con.prepareStatement(trnsinsert);
            
            prepupdateqry.execute();
            inserttransc.execute();
            
            con.close();
            return (amt);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return(0);      
    }

    @Override
    public int balance(int accno) throws RemoteException {
        int amt;   
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "1234567890");
               
            Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select amount from accounts where account_number='"+accno+"'");
                while(rs.next()){
                    amt=Integer.parseInt(rs.getString(1));

            con.close();
            return (amt);
        } 
        
    }   catch (SQLException ex) {
            Logger.getLogger(BankImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(0);
    }
}
