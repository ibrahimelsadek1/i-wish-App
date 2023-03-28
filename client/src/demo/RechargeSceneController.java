/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class RechargeSceneController implements Initializable {

    @FXML
    private TextField cardNum;
    @FXML
    private TextField CVV;
    @FXML
    private DatePicker ExpireDate;
    @FXML
    private TextField Amount;
    
    static Socket socket1;
    static PrintStream ps1 ;
    static InputStream is1;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void recharge(){
                String amount = Amount.getText();
                if (amount == null || amount.isEmpty() || !amount.matches("\\d+(\\.\\d+)?")) {
                    JOptionPane.showMessageDialog(null,"please enter a valid amount (numbers only)");
                    return;
                }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Transaction");
                    String s = "Are you sure you want to recharge your balance with"+amount+"?";
                    alert.setContentText(s);
                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        try {
                            socket1=new Socket("127.0.0.1", 5005);
                            ps1 = new PrintStream(socket1.getOutputStream());
                //            is1 = socket1.getInputStream();
                            ps1.println("Recharge");
                            ps1.println(DataHolder.getInstance().getData()); //user
                            ps1.println(Amount.getText());   // amount
                            JOptionPane.showMessageDialog(null,"Recharging has been done succssfully, refresh your profile");
                        } catch (IOException ex) {
                            Logger.getLogger(RechargeSceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        finally{
                            try {
                                ps1.close();
                                socket1.close();
                            } catch (IOException ex) {
                                Logger.getLogger(RechargeSceneController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }  
                    }     
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
