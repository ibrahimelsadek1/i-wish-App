/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser;

import java.io.Serializable;

/**
 *
 * @author ibrahim
 */
public class userFriends implements Serializable {
    private String friend_fname;
    private String friend_lname;
    private String friend_email;
    private String img_url;

    public userFriends(String friend_fname, String friend_lname, String friend_email ,String img_url) {
        this.friend_fname = friend_fname;
        this.friend_lname = friend_lname;
        this.friend_email = friend_email;
         this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getFriend_fname() {
        return friend_fname;
    }

    public void setFriend_fname(String friend_fname) {
        this.friend_fname = friend_fname;
    }

    public String getFriend_lname() {
        return friend_lname;
    }

    public void setFriend_lname(String friend_lname) {
        this.friend_lname = friend_lname;
    }

    public String getFriend_email() {
        return friend_email;
    }

    public void setFriend_email(String friend_email) {
        this.friend_email = friend_email;
    }


    
    
}
