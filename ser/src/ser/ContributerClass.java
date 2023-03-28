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
public class ContributerClass implements Serializable {
    private String email;
    private String firstname;
    private int total_cont;

    public ContributerClass(String firstname, String email, int total_cont) {
        this.firstname = firstname;
        this.email = email;
        this.total_cont = total_cont;
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotal_cont() {
        return total_cont;
    }

    public void setTotal_cont(int total_cont) {
        this.total_cont = total_cont;
    }
    
    
}
