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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class MywishlistsceneController implements Initializable {

    @FXML
    private ImageView wishimg;
    @FXML
    private Label ProdNameLabel;
    @FXML
    private Label PriceLabel;
    private Label CurrContlabel;
    @FXML
    private Label CurrContribution;
    @FXML
    private Text itemID;
    @FXML
    private HBox hboxcontain;
    @FXML
    private Rectangle profRect;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    static InputStream is1;
   
    
    public void setData(int id, String name , int Price ,int Cur_cont,Image img){
        profRect.setFill(new ImagePattern(img));
        ProdNameLabel.setText(name);
        PriceLabel.setText(String.valueOf(Price));
        CurrContribution.setText(String.valueOf(Cur_cont));
        itemID.setText(String.valueOf(id));
    }
    
    @FXML
    public void removeData(ActionEvent event){
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Delete Items");
                    String s = "Are you sure you want to delete this wish from your list ?";
                    alert.setContentText(s);
                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        try {
                            socket1=new Socket("127.0.0.1", 5005);
                            ps1 = new PrintStream(socket1.getOutputStream());
                            ps1.println("deleteWish");
                            ps1.println(DataHolder.getInstance().getData()); ///owner
                            ps1.println(itemID.getText()); /// item
                            VBox parentVBox = (VBox) hboxcontain.getParent();
                            parentVBox.getChildren().remove(hboxcontain);
                        } catch (IOException ex) {
                            Logger.getLogger(MywishlistsceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        finally{
                            try {
                                ps1.close();
                                socket1.close();
                            } catch (IOException ex) {
                                Logger.getLogger(MywishlistsceneController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }                      
     }
     
    @FXML
     public void viewCont(){
        try {
            DataHolder.getInstance().setItem_id(itemID.getText());
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
