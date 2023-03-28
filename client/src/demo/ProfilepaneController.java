/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ser.UserData;
import ser.UserItems;
import ser.User_Wish_List;


/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class ProfilepaneController implements Initializable {

    @FXML
    private Label mobileLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private ImageView perimg;
    @FXML
    private Label ProfilenameLabel;
    @FXML
    private VBox MywishlistVbox;
    @FXML
    private ScrollPane myitemspane;
    @FXML
    private VBox myItemsVbox;
        String resopnse;
    String email;
    
    
    static Socket socket;
    static DataInputStream dis ;
    static PrintStream ps ;
    @FXML
    private Text balanceLabel;
    @FXML
    private Circle profcir;
    
    @FXML
    public void rechargePanel(){
    
        try {
            FXMLLoader fxmloader = new FXMLLoader(getClass().getResource("RechargeScene.fxml"));
            Parent my_root1 = (Parent) fxmloader.load();
            Stage stag2=new Stage();
            stag2.setTitle("Recharge Your Balance");
            stag2.setScene(new Scene(my_root1));
            stag2.show();
        } catch (IOException ex) {
            Logger.getLogger(ProfilepaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String user = DataHolder.getInstance().getData();
            socket=new Socket("127.0.0.1", 5005);
            ps = new PrintStream(socket.getOutputStream());
            InputStream is = socket.getInputStream();
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    try {
                        
                        ps.println("userwishlist");
                        ps.println(user);
                        Thread.sleep(100);
                        byte[] vecBytes = new byte[is.available()];
                        is.read(vecBytes);
                        System.out.print(vecBytes);
                        ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        ArrayList<User_Wish_List> arr;
                        arr = (ArrayList<User_Wish_List>)ois.readObject();
                        System.out.println(arr);
                        Image wishImg;
                        for (int i = 0; i < arr.size(); i++){
                            
                            if (arr.get(i).getItem_img_url() == null ){
                              wishImg=new Image(getClass().getResourceAsStream("icons/items.png"));
                            }
                            else { wishImg=new Image(getClass().getResourceAsStream(arr.get(i).getItem_img_url()));}
                            
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("mywishlistscene.fxml"));
                            try {
                                HBox hBox1= fxmlLoader.load();
                                MywishlistsceneController cic1 = fxmlLoader.getController();
//                                System.out.println(arr.get(i).getItem_name()+arr.get(i).getPrice()+arr.get(i).getCurr_cont());
                                cic1.setData(arr.get(i).getItem_id(),arr.get(i).getItem_name(),arr.get(i).getPrice(),arr.get(i).getCurr_cont(),wishImg);
                                MywishlistVbox.getChildren().add(hBox1);
                            }
                            catch (IOException e) {
                                e.printStackTrace();}}
                        /////////////////////////////////////////////
                        ps.println("useritems");
                        ps.println(user);
                        Thread.sleep(1000);
                        byte[] vecBytes1 = new byte[is.available()];
                        is.read(vecBytes1);
                        System.out.print(vecBytes1);
                        ByteArrayInputStream bis1 = new ByteArrayInputStream(vecBytes1);
                        ObjectInputStream ois1 = new ObjectInputStream(bis1);
                        ArrayList<UserItems> arr1;
                        arr1 = (ArrayList<UserItems>)ois1.readObject();
                        Image itemImg;
                        for (int i = 0; i < arr1.size(); i++){
                            
                            if (arr1.get(i).getImg_url() == null ){
                              itemImg=new Image(getClass().getResourceAsStream("icons/items.png"));
                            }
                            else { itemImg=new Image(getClass().getResourceAsStream(arr1.get(i).getImg_url()));}
                                                                
                            FXMLLoader fxmlLoader1 = new FXMLLoader();
                            fxmlLoader1.setLocation(getClass().getResource("myitemsScene.fxml"));
                            try {
                                HBox hBox= fxmlLoader1.load();
                                myitemscontroller cic = fxmlLoader1.getController();
//                                cic.setEmail(paneEmail.getText());
                                cic.setData(arr1.get(i).getItem_id(),arr1.get(i).getItem_name(),arr1.get(i).getPrice(),arr1.get(i).getCat(),itemImg);
                                myItemsVbox.getChildren().add(hBox);
                                
                            }
                            catch (IOException e) {e.printStackTrace();}}
                        ///////////////////////////////////////
                        ps.println("userData");
                        ps.println(user);
                        Thread.sleep(1000);
                        byte[] vecBytes2 = new byte[is.available()];
                        is.read(vecBytes2);
                        System.out.print(vecBytes2);
                        ByteArrayInputStream bis2 = new ByteArrayInputStream(vecBytes2);
                        ObjectInputStream ois2 = new ObjectInputStream(bis2);
                        ArrayList<UserData> arr2;
                        arr2 = (ArrayList<UserData>)ois2.readObject();

                        Image profImg;
                        for (int i = 0; i < arr2.size(); i++){
                            System.out.print(arr2.get(i).getImg_url());
                            if (arr2.get(i).getImg_url() == null ){
                             profImg=new Image(getClass().getResourceAsStream("icons/user2.png"));
                            }
                            else {
                                profImg=new Image(getClass().getResourceAsStream(arr2.get(i).getImg_url()));}
                        balanceLabel.setText(String.valueOf(arr2.get(i).getBalance()));
                        DataHolder.getInstance().setBalance(Integer.valueOf(String.valueOf(arr2.get(i).getBalance())));//////////// balane variable
                        profcir.setFill(new ImagePattern(profImg));
//                        perimg.setImage(profImg);
                        ProfilenameLabel.setText(arr2.get(0).getFname());
                        mobileLabel.setText(String.valueOf(arr2.get(0).getMobile())); } 
                        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                            Logger.getLogger(ProfilepaneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }        
            });
        }
        catch (IOException ex) {
               Logger.getLogger(ProfilepaneController.class.getName()).log(Level.SEVERE, null, ex);
          }
 
    }
}    

    

