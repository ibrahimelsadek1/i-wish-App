/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author ibrahim
 */
public class UsersDAL {
    
    Connection con=null;
    ResultSet rs=null;
//    ArrayList<User_Wish_List> wish_list = new ArrayList<User_Wish_List>();
    
    public UsersDAL(Connection con) throws SQLException{
            this.con=con;
    }
    
    public boolean authUser(String user,String password) throws SQLException{
            PreparedStatement selectuser = con.prepareStatement("SELECT * FROM users where user_email=? and user_PASSWORD=?");
            selectuser.setString(1,user);
            selectuser.setString(2,password);
            rs = selectuser.executeQuery() ;
            if (rs.next()) {
                return true;
            }
            else{
                return false;
            }
    }
    

    
    public ArrayList<User_Wish_List> getUserWishList(String email) throws SQLException{
            ArrayList<User_Wish_List> wish_list = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select i.item_id ,i.item_name ,  i.item_price, u.CURRENT_CONTRIBUTION , i.Item_CATEGORY,i.item_img_url  from items i , user_wish_list u where i.item_id =u.item_id  and  u.user_email=?");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
//            User_Wish_List obj = new User_Wish_List(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5),rs.getBytes(6));
            User_Wish_List obj = new User_Wish_List(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5),rs.getString(6));
            wish_list.add(obj);
            }
            return wish_list;
    }
    
    
    public ArrayList<UserData> getUserData(String email) throws SQLException{
            ArrayList<UserData> user_data = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("SELECT * FROM users where user_email=?");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
            UserData obj = new UserData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(8),rs.getInt(6),rs.getString(7));
//                       UserData obj = new UserData(rs.getString(1),rs.getString(2),rs.getString(3),null,rs.getInt(6),rs.getString(7));
            user_data.add(obj);
            }
            return user_data;
    }
    
    
    public ArrayList<ContributerClass> getContributers(String email,String item_id) throws SQLException{
            ArrayList<ContributerClass> contrs = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("SELECT USERS.USER_FNAME ,USERS.USER_EMAIL ,SUM(CONTRIBUTIONS.CON_AMOUNT)  \n" +
                "FROM USERS \n" +
                "JOIN CONTRIBUTIONS  \n" +
                "on USERS.USER_EMAIL=CONTRIBUTIONS.FROM_USER_EMAIL \n" +
                "where TO_USER_EMAIL =? and ITEM_ID=?\n" +
                "group by USERS.USER_FNAME ,USERS.USER_EMAIL ,CONTRIBUTIONS.ITEM_ID ");
            selectuser.setString(1,email);
            selectuser.setInt(2,Integer.valueOf(item_id));
            rs = selectuser.executeQuery();
            while (rs.next()){
                 System.out.println(rs.getString(1)+rs.getString(2)+rs.getInt(3));
            ContributerClass obj = new ContributerClass(rs.getString(1),rs.getString(2),rs.getInt(3));
//                       UserData obj = new UserData(rs.getString(1),rs.getString(2),rs.getString(3),null,rs.getInt(6),rs.getString(7));
            contrs.add(obj);
            }
            selectuser.close();
            rs.close();
            return contrs;
    }
    
    public ArrayList<UserData> getFriendRequests(String email) throws SQLException{
            ArrayList<UserData> request = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select   U.USER_FNAME ,  F.FRIEND_request_EMAIL ,u.img_url \n" +
                    "from FRIEND_REQUEST F , users U \n" +
                    "where  U.USER_EMAIL= F.FRIEND_request_EMAIL and f.USER_EMAIL=?");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
            UserData obj = new UserData(rs.getString(2),rs.getString(1),null,rs.getString(3),0,null);
//                       UserData obj = new UserData(rs.getString(1),rs.getString(2),rs.getString(3),null,rs.getInt(6),rs.getString(7));
            request.add(obj);
            }
            selectuser.close();
            rs.close();
            return request;
    }
    
        
    public ArrayList<UserItems> getUserItems(String email) throws SQLException{
            ArrayList<UserItems> user_items = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select i.item_id ,i.item_name ,  i.item_price, i.Item_CATEGORY,i.item_img_url  from items i , user_owned_list u where i.item_id =u.item_id  and  u.user_email=?");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
//            UserItems obj = new UserItems(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getBytes(5));
            UserItems obj = new UserItems(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5));
            user_items.add(obj);
            }
            selectuser.close();
            rs.close();
            return user_items;
    }
       public ArrayList<UserItems> getAllItems(String email) throws SQLException{
            ArrayList<UserItems> user_items = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select * from items where item_id not in ( select item_id from user_wish_list where user_email = ?)");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            
            while (rs.next()){
//            UserItems obj = new UserItems(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getBytes(5));
            UserItems obj = new UserItems(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(6));
            user_items.add(obj);
            }
            return user_items;
    }
    
    public ArrayList<userFriends> getUserFriends(String email) throws SQLException{
            ArrayList<userFriends> user_friends = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select user_FNAME,user_LNAME ,user_email ,img_url from users \n" +
                    "where user_email in (select friend_email from friends where user_email = ? \n" +
                    "union\n" +
                    "select user_email from friends where friend_email = ?)");
            selectuser.setString(1,email);
            selectuser.setString(2,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
            userFriends obj = new userFriends(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
            user_friends.add(obj);
            }
            selectuser.close();
            rs.close();
            return user_friends;
    }
    public ArrayList<userFriends> getAllUsers(String email) throws SQLException{
            ArrayList<userFriends> all_users = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select user_FNAME , user_Lname,users.user_email from users where users.user_email not in(select friend_email from friends where user_email = ?\n" +
                    "union\n" +
                    "select user_email from friend_request where FRIEND_REQUEST_EMAIL = ?\n" +
                    "union\n" +
                    "select FRIEND_REQUEST_EMAIL from friend_request where user_email = ? )  and users.user_email != ?");
            selectuser.setString(1,email);
            selectuser.setString(2,email);
            selectuser.setString(3,email);
            selectuser.setString(4,email);
            rs = selectuser.executeQuery();
            while (rs.next()){
            userFriends obj = new userFriends(rs.getString(1),rs.getString(2),rs.getString(3),"");
            all_users.add(obj);}
            selectuser.close();
            rs.close();
            return all_users;
    }
    
    public int addUser(String fname,String lname,String email,String mobile,String password,String img_url) throws SQLException{
                    try {
                        PreparedStatement insertpst = con.prepareStatement("INSERT INTO USERS (USER_EMAIL, USER_FNAME, USER_LNAME, user_password ,user_img, USER_balance, mobile , img_url) VALUES(?,?,?,?,null,0,?,?)");
                        insertpst.setString(1,email);
                        insertpst.setString(2,fname);
                        insertpst.setString(3,lname);
                        insertpst.setString(4,password);
                        insertpst.setString(5,mobile);
                        insertpst.setString(6,img_url);
                        insertpst.executeUpdate();
                        return 1;
                        
                    } 
                    catch (NumberFormatException nex) {
                        return 2;
                    }
                    catch (SQLIntegrityConstraintViolationException se) {
                        return 3;
                    }   
                    catch (SQLException ex) {
                        return 4;
                    }
                    
    }    
    public int removeFriend(String user,String friend) {
                    
        try {
            PreparedStatement deletepst = con.prepareStatement("delete from friends where ( user_email=? or friend_email=?) and (friend_email =? or user_email=?)");
            deletepst.setString(1,user);
            deletepst.setString(2,user);
            deletepst.setString(3,friend);
            deletepst.setString(4,friend);
            deletepst.executeUpdate();
            deletepst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
                     
    }
    
    public int acceptFriendRequest(String user,String friend) {
                    
        try {
            PreparedStatement insertst = con.prepareStatement("insert into friends (USER_EMAIL,friend_EMAIL) values(?,?)");
            insertst.setString(1,user);
            insertst.setString(2,friend);
            insertst.executeUpdate();
            
            PreparedStatement accstm = con.prepareStatement("delete from friend_request where user_email=? and FRIEND_REQUEST_EMAIL =?");
            accstm.setString(1,user);
            accstm.setString(2,friend);
            accstm.executeUpdate();
            insertst.close();
            accstm.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
                     
    }
    
    public int rechargeBalance(String user,String amount) {
                    
        try {
            PreparedStatement insertst = con.prepareStatement("update users set user_balance = user_balance+? where user_email = ?");
            insertst.setInt(1,Integer.valueOf(amount));
            insertst.setString(2,user);
            insertst.executeUpdate();
            insertst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
                     
    }
    
    
    public int declineFriendRequest(String user,String friend) {
                    
        try {
            PreparedStatement insertst = con.prepareStatement("delete from friend_request where user_email=? and FRIEND_REQUEST_EMAIL =?");
            insertst.setString(1,user);
            insertst.setString(2,friend);
            insertst.executeUpdate();
            insertst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
                     
    }
    public int wishItem(String user,String item_id) {
                    
        try {
            PreparedStatement deletepst = con.prepareStatement("insert into user_wish_list values (?,?,0)");
            deletepst.setString(1,user);
            deletepst.setInt(2,Integer.valueOf(item_id));
            deletepst.executeUpdate();
            deletepst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }               
    }
    
    public int deleteWish(String user,String item_id) {
                    
        try {
            PreparedStatement deletepst = con.prepareStatement("delete from user_wish_list where user_email=? and item_id =?");
            deletepst.setString(1,user);
            deletepst.setInt(2,Integer.valueOf(item_id));
            deletepst.executeUpdate();
            deletepst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }               
    }
    
    public int insertItem(String it_name,String price,String cat,String url) {
        try {
            PreparedStatement insert1 = con.prepareStatement("INSERT INTO ITEMS (ITEM_ID, ITEM_NAME, ITEM_PRICE, ITEM_CATEGORY, ITEM_IMG_URL) VALUES (items_seq.nextval , ? , ? , ?,?)");
            insert1.setString(1,it_name);
            insert1.setInt(2,Integer.valueOf(price));
            insert1.setString(3,cat);
            insert1.setString(4,url);
            insert1.executeUpdate();
            con.commit();

            return 1;
        } catch (SQLException ex) {
            return 0;
        }               
    }
    
    public int friendRequest(String to ,String from) {
                    
        try {
            PreparedStatement deletepst = con.prepareStatement("insert into friend_request values (?,?,sysdate)");
            deletepst.setString(1,to);
            deletepst.setString(2,from);
            deletepst.executeUpdate();
            deletepst.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }               
    }
    
    public int contribute(String user,String friend,String item,String amount) throws SQLException{
                    try {
                        PreparedStatement insertpst = con.prepareStatement("INSERT INTO CONTRIBUTIONS (FROM_USER_EMAIL, TO_USER_EMAIL, ITEM_ID, CON_AMOUNT, ON_DATE) VALUES (?,?, ?, ?, sysdate)");
                        insertpst.setString(1,user);
                        insertpst.setString(2,friend);
                        insertpst.setInt(3,Integer.valueOf(item));
                        insertpst.setInt(4,Integer.valueOf(amount));
                        insertpst.executeUpdate();
                        return 1;
                    } 
                    catch (NumberFormatException nex) {
                        return 2;
                    }
                    catch (SQLIntegrityConstraintViolationException se) {
                        return 3;
                    }   
                    catch (SQLException ex) {
                        return 4;
                    }
    }
    public String getNotification(String user ) throws SQLException {
        String notification=null ;
        String id ;
        PreparedStatement getOneNotif=null;
        PreparedStatement updateNotif=null;
        ResultSet rs3=null;
                    
        try {
            getOneNotif = con.prepareStatement("select noti_id , noti from notifications where user_email =? and notified='no' and rownum=1");
            getOneNotif.setString(1,user);
            rs3=getOneNotif.executeQuery();

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                        LocalDateTime now = LocalDateTime.now();  
                        System.out.println(dtf.format(now)); 
            
            if (rs3.next()) {
                notification = rs3.getString(2);
                id = rs3.getString(1);
                updateNotif = con.prepareStatement("update notifications set notified = 'yes' where user_email= ? and noti_id=?");
                updateNotif.setString(1,user);
                updateNotif.setInt(2,Integer.valueOf(id));
                updateNotif.executeUpdate();
                getOneNotif.close();
                updateNotif.close();
                rs3.close();

            } else {notification="NoN";}
            System.out.println("notif "+notification);

        } catch (SQLException ex) {
            System.out.println("notif user sql exc");
            return "NoN";  
        }
        
        
        
        return notification;
    }
    
    public ArrayList<Notification> getAllNotifications(String email) throws SQLException{
            ArrayList<Notification> user_notifications = new ArrayList<>();
            PreparedStatement selectuser = con.prepareStatement("select noti from notifications  where user_email =? order by noti_date desc");
            selectuser.setString(1,email);
            rs = selectuser.executeQuery();
            
            while (rs.next()){
            Notification obj = new Notification(rs.getString(1));
            user_notifications.add(obj);
            }
            selectuser.close();
            rs.close();
            return user_notifications;
    }
}
