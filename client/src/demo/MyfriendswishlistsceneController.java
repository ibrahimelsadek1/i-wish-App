/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class MyfriendswishlistsceneController implements Initializable {

    @FXML
    private ImageView wishimg;
    @FXML
    private Label ProdNameLabel;
    @FXML
    private Label PriceLabel;
    private Label CurrContlabel;
    @FXML
    private Label CurrContribution;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    @FXML
    private Text itemIdLabel;
    @FXML
    private TextField amountLabel;
    @FXML
    private HBox container;
    @FXML
    private Pane con;
   
    
    public void setData(int item_id ,String prod , int Price ,int Cur_cont,Image img){
        wishimg.setImage(img);
        itemIdLabel.setText(String.valueOf(item_id));
        ProdNameLabel.setText(prod);
        PriceLabel.setText(String.valueOf(Price));
        CurrContribution.setText(String.valueOf(Cur_cont));
        
    }
    @FXML
    public void contribute() {
    int amt;
    try {
        amt = Integer.valueOf(amountLabel.getText());
        int bal = DataHolder.getInstance().getBalance();
        int price = Integer.valueOf(PriceLabel.getText()); 
        int cont = Integer.valueOf(CurrContribution.getText());

        if (bal >= amt) {
            // to do
            if (amt > (price - cont)) {
                System.out.println("amount more than needed");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Contribution not completed");
                alert.setHeaderText("Amount more than required");
                String s = "The amount needed to help your friend is just " + (price - cont);
                alert.setContentText(s);
                alert.show();
            } else {
                try {
                    socket1 = new Socket("127.0.0.1", 5005);
                    ps1 = new PrintStream(socket1.getOutputStream());
                    InputStream is1 = socket1.getInputStream();
                    ps1.println("Contribute");
                    ps1.println(DataHolder.getInstance().getData());
                    ps1.println(DataHolder.getInstance().getFriend());
                    ps1.println(itemIdLabel.getText());
                    ps1.println(amountLabel.getText());
                    CurrContribution.setText(String.valueOf(amt + cont));
                    DataHolder.getInstance().setBalance(bal - amt);
                    JOptionPane.showMessageDialog(null, "Succesful contribution with " + amt + " and your balance now is  " + (bal-amt));
                } catch (IOException ex) {
                    Logger.getLogger(MyfriendswishlistsceneController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        ps1.close();
                        socket1.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MyfriendswishlistsceneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            System.out.println("message that amount not enough");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Contribution not completed");
            alert.setHeaderText("Your Balance is not enough");
            String s = "You only have " + bal + " $ in your balance \nYou need to recharge your balance";
            System.out.println(bal);
            alert.setContentText(s);
            alert.show();
        }
    } catch (NumberFormatException e) {
        System.out.println("Invalid input, amount should be an integer");
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText("Amount should be a number");
        alert.setContentText("Please enter a valid number for the amount");
        alert.showAndWait();
        return;
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
