import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class BankServer
{
public static void main(String args[])
{
try
{
BankImpl bankimpl=new BankImpl();

Registry reg=LocateRegistry.createRegistry(3159);
reg.rebind("BankServer", bankimpl);
System.out.println("Server is running!!");


// Naming.rebind("BankServer",bankimpl);
// System.out.println("Serevr is ready");
}

catch(Exception e)
{
System.out.println("Exception :"+e);
}
}
}