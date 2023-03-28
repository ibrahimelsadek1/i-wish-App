/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleDriver;


/**
 *
 * @author ibrahim
 */
public class Ser {
    
    ServerSocket ser;
    Connection con;
    
    public Ser(){
        try {
            ser = new ServerSocket(5005);
            DriverManager.registerDriver(new OracleDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "HR", "hr");
            while (true){
                Socket s = ser.accept();
                new ClientHandler(s,con,ser);
            }
        } catch (IOException ex) {
            Logger.getLogger(Ser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Ser.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public Connection gerSerCon(){
        return this.con;  
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Ser();
    }
}


//////// clients handler to handle mutliple clients requests at the same time
class ClientHandler extends Thread
    {
    DataInputStream dis;
    PrintStream ps;
    Connection con=null;
    Socket cs;
    int cur_client;
    
    static Vector<ClientHandler> clientsVector = new Vector<ClientHandler>();
    
    /// object from UsersDAL.java class to comuunicate with database through it
    UsersDAL au;


    public ClientHandler(Socket cs,Connection con,ServerSocket ser)
    {
        try {
            this.con=con;
            this.cs=cs;
            dis = new DataInputStream(cs.getInputStream());
            ps = new PrintStream(cs.getOutputStream());
            clientsVector.add(this);
            cur_client=clientsVector.indexOf(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(Ser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        public void run()
        {
        while(true)
        {
            try {
                String reqType = dis.readLine();
                
                //////////// this switch recieves every request as string and go through specific case to be executed
                switch(reqType){
                    case "loginReq": ////////// login case -- recieved from scene1controller.java
                                String user = dis.readLine();
                                String pass = dis.readLine();
                                au=new UsersDAL(con);
                                if (au.authUser(user,pass)){ 
                                System.out.println(user);
                                System.out.println(pass);
                                System.out.println(clientsVector);
                                clientsVector.get(cur_client).ps.println("loginSuccess");
                                } else {clientsVector.get(cur_client).ps.println("failed"); System.out.println("failed");};
                    break;
                                
                    case "registerReq": ////////// login case -- to be recieved from regiteringscenecontroller.java
                                String fname = dis.readLine();
                                String lname = dis.readLine();
                                String email = dis.readLine();
                                String mobile = dis.readLine();
                                String password = dis.readLine();
                                String img_url= dis.readLine();
                                UsersDAL ins=new UsersDAL(con);
                                switch (ins.addUser(fname,lname,email,mobile,password,img_url)) {
                                    case 1:
                                        clientsVector.get(cur_client).ps.println("registerSuccess");
                                        break;
                                    case 2:
                                        clientsVector.get(cur_client).ps.println("wrongFormat");
                                        break;
                                    case 3:
                                        clientsVector.get(cur_client).ps.println("ExistedUser");
                                        break;
                                    default:
                                        break;
                                }
                    break;
                    case "userwishlist": //////////////
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(bos);
                                System.out.println(au.getUserWishList(email));
                                oos.writeObject(au.getUserWishList(email));
                                oos.flush();
                                byte[] rowSetBytes = bos.toByteArray();
                                OutputStream os = cs.getOutputStream();
                                os.write(rowSetBytes);
                                os.flush();
                                ;
                    break;
                    case "useritems": //////////////////
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                                ObjectOutputStream oos1 = new ObjectOutputStream(bos1);
                                System.out.println(au.getUserItems(email));
                                oos1.writeObject(au.getUserItems(email));
                                oos1.flush();
                                byte[] rowSetBytes1 = bos1.toByteArray();
                                OutputStream os1 = cs.getOutputStream();
                                os1.write(rowSetBytes1);
                                os1.flush();
                                
                    break;
                    case "userData": ///////////////////
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                                ObjectOutputStream oos2 = new ObjectOutputStream(bos2);
                                System.out.println(au.getUserData(email));
                                oos2.writeObject(au.getUserData(email));
                                oos2.flush();
                                byte[] rowSetBytes2 = bos2.toByteArray();
                                OutputStream os2 = cs.getOutputStream();
                                os2.write(rowSetBytes2);
                                os2.flush();
                                
                    break;
                    case "friendRequests": ///////////////////
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos6 = new ByteArrayOutputStream();
                                ObjectOutputStream oos6 = new ObjectOutputStream(bos6);
                                System.out.println(au.getFriendRequests(email));
                                oos6.writeObject(au.getFriendRequests(email));
                                oos6.flush();
                                byte[] rowSetBytes6 = bos6.toByteArray();
                                OutputStream os6 = cs.getOutputStream();
                                os6.write(rowSetBytes6);
                                os6.flush();
                                
                    break;
                    case "userfriends":
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
                                ObjectOutputStream oos3 = new ObjectOutputStream(bos3);
                                System.out.println(au.getUserFriends(email));
                                oos3.writeObject(au.getUserFriends(email));
                                oos3.flush();
                                byte[] rowSetBytes3 = bos3.toByteArray();
                                OutputStream os3 = cs.getOutputStream();
                                os3.write(rowSetBytes3);
                                os3.flush();
                    break;
                    case "allusers":
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
                                ObjectOutputStream oos4 = new ObjectOutputStream(bos4);
                                System.out.println(au.getAllUsers(email));
                                oos4.writeObject(au.getAllUsers(email));
                                oos4.flush();
                                byte[] rowSetBytes4 = bos4.toByteArray();
                                OutputStream os4 = cs.getOutputStream();
                                os4.write(rowSetBytes4);
                                os4.flush();
                    break;
                    case "allitems":
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                                ObjectOutputStream oos5 = new ObjectOutputStream(bos5);
                                oos5.writeObject(au.getAllItems(email));
                                oos5.flush();
                                byte[] rowSetBytes5 = bos5.toByteArray();
                                OutputStream os5 = cs.getOutputStream();
                                os5.write(rowSetBytes5);
                                os5.flush();
                    break;
                    
                    case "allNotifications":
                                email = dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos11 = new ByteArrayOutputStream();
                                ObjectOutputStream oos11 = new ObjectOutputStream(bos11);
                                oos11.writeObject(au.getAllNotifications(email));
                                oos11.flush();
                                byte[] rowSetBytes11 = bos11.toByteArray();
                                OutputStream os11 = cs.getOutputStream();
                                os11.write(rowSetBytes11);
                                os11.flush();
                    break;
                    case "contributers":
                                email = dis.readLine();
                                String item_id2= dis.readLine();
                                au=new UsersDAL(con);
                                ByteArrayOutputStream bos8 = new ByteArrayOutputStream();
                                ObjectOutputStream oos8 = new ObjectOutputStream(bos8);
//                                System.out.println(au.getAllUsers(email));
                                oos8.writeObject(au.getContributers(email,item_id2));
                                oos8.flush();
                                byte[] rowSetBytes8 = bos8.toByteArray();
                                OutputStream os8 = cs.getOutputStream();
                                os8.write(rowSetBytes8);
                                os8.flush();
                    break;
                    case "removefriend":
                                email = dis.readLine();
                                String friend_email = dis.readLine();
                                au=new UsersDAL(con);
                                int r = au.removeFriend(email,friend_email);
                    break;
                    case "acceptrequest":
                                email = dis.readLine();
                                String f1_email = dis.readLine();
                                au=new UsersDAL(con);
                                int p = au.acceptFriendRequest(email,f1_email);
                    break;
                    case "submitItem":
                                email = dis.readLine();
                                String it_name = dis.readLine();
                                String price = dis.readLine();
                                String cat = dis.readLine();
                                String url = dis.readLine();
                                au=new UsersDAL(con);
                                int o = au.insertItem(it_name,price,cat,url);
                    break;
                    
                    case "Recharge":
                                email = dis.readLine();
                                String amount= dis.readLine();
                                au=new UsersDAL(con);
                                int q = au.rechargeBalance(email,amount);
                    break;
                    
                    case "declinerequest":
                                email = dis.readLine();
                                String f7_email = dis.readLine();
                                au=new UsersDAL(con);
                                int tt = au.declineFriendRequest(email,f7_email);
                                
                    break;
                    case "Contribute":
                                email = dis.readLine();
                                String f_email = dis.readLine();
                                String item_id=dis.readLine();
                                String amt = dis.readLine();
                                au=new UsersDAL(con);
                                int y = au.contribute(email,f_email,item_id,amt);
                    break;
                    case "wishitem":
                                email = dis.readLine();
                                String item_id_w = dis.readLine();
                                au=new UsersDAL(con);
                                int z = au.wishItem(email,item_id_w);
                    break;
                    case "deleteWish":
                                email = dis.readLine();
                                String item_id_del = dis.readLine();
                                au=new UsersDAL(con);
                                int ee = au.deleteWish(email,item_id_del);
                    break;
                    
                    case "addfriend":
                                String from = dis.readLine();
                                String to = dis.readLine();
                                au=new UsersDAL(con);
                                int a = au.friendRequest(to,from);

                    break;
                    case "getNotification": //////////
                                String user__ = dis.readLine();
                                au=new UsersDAL(con);
                                ps.println(au.getNotification(user__));
                    break;
                }
     /////////////////// end of switch
            } catch (SocketException se) {
                try {
                    dis.close();
                    ps.close();
                    this.stop();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
            
            catch (IOException ex) {
                Logger.getLogger(Ser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (NullPointerException nx) {
                return;
            }
        }
        }    
}
    
    


    

