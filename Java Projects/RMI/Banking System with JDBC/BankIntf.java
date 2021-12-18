import java.rmi.*;
public interface BankIntf extends Remote
{
int withdraw(int a,int amt,int accno)throws RemoteException;
int deposit(int b,int amt,int accno)throws RemoteException;
int balance(int accno)throws RemoteException;
}