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
import javafx.scene.control.Button;
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
public class MarketItemController implements Initializable {

    @FXML
    private ImageView itemImage;
    @FXML
    private Label ProdNameLabel;
    @FXML
    private Label categorylabel;
    @FXML
    private Label PriceLabel;
    @FXML
    private Button WishBt;
    @FXML
    private Text itemID;
    @FXML
    private VBox container;
    
    
    /// method for setting data of every market item scene
    public void setData(int item_id ,String prod , int Price ,String cat,Image img){
//        Image itemImg=new Image(getClass().getResourceAsStream(img));
        itemImage.setImage(img);
        itemID.setText(String.valueOf(item_id));
        ProdNameLabel.setText(prod);
        PriceLabel.setText(String.valueOf(Price));
        categorylabel.setText(cat);
        
    }
    
    static Socket socket1;
    static PrintStream ps1 ;
    
    
    //// handler from wishitem action from user
    @FXML
    public void wishItem(){
        
        try {
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            ps1.println("wishitem");
            ps1.println(DataHolder.getInstance().getData());
//            ps1.println(DataHolder.getInstance().getFriend());
            ps1.println(itemID.getText());
            HBox parenthBox = (HBox) container.getParent();
            parenthBox.getChildren().remove(container);
        } catch (IOException ex) {
            Logger.getLogger(ItemsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                ps1.close();
                socket1.close();
            } catch (IOException ex) {
                Logger.getLogger(MarketItemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
