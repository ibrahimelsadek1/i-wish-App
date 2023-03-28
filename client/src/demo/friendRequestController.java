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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class friendRequestController implements Initializable {

    private ImageView wishimg;
    private Label ProdNameLabel;
    private Label PriceLabel;
    private Label CurrContlabel;
    private Label CurrContribution;
    private Text itemID;
    @FXML
    private ImageView reqImg;
    @FXML
    private Label ReqName;
    @FXML
    private Label ReqEmail;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    static InputStream is1;
    @FXML
    private HBox reqContainer;
    
    public void setData(String fname , String email ,Image img){
      
        ReqName.setText(fname);
        ReqEmail.setText(email);
          reqImg.setImage(img);
    }
    
        @FXML
    public void acceptRequest(){
    
        try {
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            ps1.println("acceptrequest");
            ps1.println(DataHolder.getInstance().getData()); /// come to (owner)
            ps1.println(ReqEmail.getText()); /// come from (sender)
            VBox parentVBox = (VBox) reqContainer.getParent();
            parentVBox.getChildren().remove(reqContainer);
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    @FXML
    public void declineRequest(){
        try {
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            ps1.println("declinerequest");
            ps1.println(DataHolder.getInstance().getData()); /// come to (owner)
            ps1.println(ReqEmail.getText()); /// come from (sender)
            
            VBox parentVBox = (VBox) reqContainer.getParent();
            parentVBox.getChildren().remove(reqContainer);
        } catch (IOException ex) {
            Logger.getLogger(friendRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
