/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ibrahim
 */
public class DBconnection  {
    
    Connection con;
    ResultSet rs;
    private static final String dbuser="root";
    private static final String dbpassword="root";
    private static final String dbhost="localhost";
    private static final int dbport=1527;
    private static final String dbname="phonebook1";
            
            
    public Connection DBconnection() throws SQLException{
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:derby://"+dbhost+":"+dbport+"/"+dbname,dbuser,dbpassword);
            PreparedStatement selectuser = con.prepareStatement("select * from USERS_AUTH where username=? and password=?");
            selectuser.setString(1,"test");
            selectuser.setString(2,"test");
            rs = selectuser.executeQuery() ;
            rs.next();
            System.out.println(rs.getString(1));
            return con;
    }

    
//    public void addUser(String user,String password) throws SQLException{
//            PreparedStatement insertuser = con.prepareStatement("insert into users_auth values (?,?)");
//            insertuser.setString(1,user);
//            insertuser.setString(2,password);
//            rs = insertuser.executeQuery() ;
//    }

    public Connection getCon() {
        return con;
    }
    public static void main(String[] args) {
        new DBconnection();
    }
}
