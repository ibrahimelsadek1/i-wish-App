/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser;

/**
 *
 * @author ibrahim
 */
public class ContributionTransaction {
    private String from_email;
    private String to_email;
    private int item_id;
    private int amount;

    public ContributionTransaction(String from_email, String to_email, int item_id, int amount) {
        this.from_email = from_email;
        this.to_email = to_email;
        this.item_id = item_id;
        this.amount = amount;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    
    
}
