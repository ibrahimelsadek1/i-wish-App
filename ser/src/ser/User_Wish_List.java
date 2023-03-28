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
public class User_Wish_List implements Serializable {
    private int item_id;
    private String item_name;
    private int price;
    private int curr_cont;
    private String cat;
    private String item_img_url;

    public User_Wish_List(int item_id, String item_name, int price, int curr_cont, String cat, String item_img_url) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.price = price;
        this.curr_cont = curr_cont;
        this.cat = cat;
        this.item_img_url = item_img_url;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_img_url() {
        return item_img_url;
    }

    public void setItem_img(String item_img_url) {
        this.item_img_url = item_img_url;
    }

 

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getCurr_cont() {
        return curr_cont;
    }

    public void setCurr_cont(int curr_cont) {
        this.curr_cont = curr_cont;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
                 
    
}
