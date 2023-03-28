/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class myitemscontroller implements Initializable {

    @FXML
    private ImageView wishimg;
    @FXML
    private Label ProdNameLabel;
    @FXML
    private Label PriceLabel;
    private Label CurrContlabel;
    @FXML
    private Label categorylabel;
    @FXML
    private Button removeBtn;
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private HBox itemsBox;
    private Text itemsowner;
    @FXML
    private Text itemIdLabel;
    
    public void setData(int item_id,String item_name, int price, String cat, Image img){                             
        wishimg.setImage(img);
        ProdNameLabel.setText(item_name);
        PriceLabel.setText(String.valueOf(price));
        categorylabel.setText(cat);
        itemIdLabel.setText(String.valueOf(item_id));
    }
     public void removeData(ActionEvent event){
            VBox parentVBox = (VBox) itemsBox.getParent();
            parentVBox.getChildren().remove(itemsBox);
     }
     
    @FXML
     public void viewCont(){
        try {
            DataHolder.getInstance().setItem_id(itemIdLabel.getText());
            FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("contributersScene.fxml"));
            Parent my_root = (Parent) fxmloader.load();
            Stage stag=new Stage();
            stag.setTitle("contributers");
            stag.setScene(new Scene(my_root));
            stag.show();
        } catch (IOException ex) {
            Logger.getLogger(myitemscontroller.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
}
