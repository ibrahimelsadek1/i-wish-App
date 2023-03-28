/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ser.User_Wish_List;
import ser.userFriends;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class FriendsPaneController implements Initializable {

    @FXML
    private VBox MywishlistVbox;
    private Text emailHolder;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    static InputStream is1;

    @FXML
    private TableView<userFriends> friendsTable1;
    @FXML
    private TableColumn<userFriends, String> fname1;
    @FXML
    private TableColumn<userFriends, String> lname1;
    @FXML
    private TableColumn<userFriends, String> email1;
    /**
     * Initializes the controller class.
     */
    public void setData(String email){
    emailHolder.setText(email);
    }
    @FXML
    public void removeFriend(){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Items");
                    String s = "Are you sure you want to delete this wish from your list ?";
                    alert.setContentText(s);
                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        try {
                                MywishlistVbox.getChildren().clear();
                                socket1=new Socket("127.0.0.1", 5005);
                                ps1 = new PrintStream(socket1.getOutputStream());
                                InputStream is1 = socket1.getInputStream();
                                ps1.println("removefriend");
                                ps1.println(DataHolder.getInstance().getData());
                                ps1.println(friendsTable1.getSelectionModel().getSelectedItem().getFriend_email());
                                friendsTable1.getItems().removeAll(friendsTable1.getSelectionModel().getSelectedItem());

                        } catch (IOException ex) {
                            Logger.getLogger(MywishlistsceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
    }
    
    @FXML
    public void getFriendWishList() throws InterruptedException{
        try {
            MywishlistVbox.getChildren().clear();
            DataHolder.getInstance().setFriend(friendsTable1.getSelectionModel().getSelectedItem().getFriend_email());
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            ps1.println("userwishlist");
            ps1.println(friendsTable1.getSelectionModel().getSelectedItem().getFriend_email());
//            ps.println(email1.getCellData(friendsTable1.getSelectionModel().getSelectedIndex()));
            System.out.print(friendsTable1.getSelectionModel().getSelectedItem().getFriend_email());
            Thread.sleep(1000);
            byte[] vecBytes = new byte[is1.available()];
            is1.read(vecBytes);
            System.out.print(vecBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList<User_Wish_List> arr;
            arr = (ArrayList<User_Wish_List>)ois.readObject();
            System.out.println(arr);
            Image wishImg;
            for (int i = 0; i < arr.size(); i++){
                  if (arr.get(i).getItem_img_url() == null ){
                              wishImg=new Image(getClass().getResourceAsStream("icons/user.png"));
                            }
                            else {wishImg=new Image(getClass().getResourceAsStream(arr.get(i).getItem_img_url()));}
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("myfriendswishlistscene.fxml"));
                try {
                    HBox hBox= fxmlLoader.load();
                    MyfriendswishlistsceneController cic = fxmlLoader.getController();
                    System.out.println(arr.get(i).getItem_name()+arr.get(i).getPrice()+arr.get(i).getCurr_cont());
                    cic.setData(arr.get(i).getItem_id(),arr.get(i).getItem_name(),arr.get(i).getPrice(),arr.get(i).getCurr_cont(),wishImg);
                    MywishlistVbox.getChildren().add(hBox);
                }
                catch (IOException e) {
                    e.printStackTrace();}}
        } catch (IOException ex) {
            Logger.getLogger(FriendsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    String user = DataHolder.getInstance().getData();
                    socket1=new Socket("127.0.0.1", 5005);
                    ps1 = new PrintStream(socket1.getOutputStream());
                    InputStream is1 = socket1.getInputStream();
                    ps1.println("userfriends");
                    ps1.println(user);
                    Thread.sleep(100);
                    byte[] vecBytes = new byte[is1.available()];
                    is1.read(vecBytes);
                    System.out.print(vecBytes);
                    ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    ArrayList<userFriends> arr5;
                    arr5 = (ArrayList<userFriends>)ois.readObject();
                    ObservableList<userFriends> user_fr = FXCollections.observableArrayList();
                                for (int i = 0; i < arr5.size(); i++){
                                    user_fr.add(arr5.get(i));
                                        }
                    fname1.setCellValueFactory(new PropertyValueFactory<>("friend_fname"));
                    lname1.setCellValueFactory(new PropertyValueFactory<>("friend_lname"));
                    email1.setCellValueFactory(new PropertyValueFactory<>("friend_email"));
                    friendsTable1.setItems(user_fr);

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FriendsPaneController.class.getName()).log(Level.SEVERE, null, ex);
                }}
        });
    }    

    
}
