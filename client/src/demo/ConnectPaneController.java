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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ser.UserData;
import ser.userFriends;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class ConnectPaneController implements Initializable {

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
    @FXML
    private Button AddFriendBT;
    @FXML
    private TextField FnameSearch;
    @FXML
    private Button SearchBTN;
    @FXML
    private TextField LnameSearch;
    @FXML
    private TextField EmailSearch;
    @FXML
    private VBox RequestsVBox;
    /**
     * Initializes the controller class.
     */
    public void setData(String email){
    emailHolder.setText(email);
    }
    
    @FXML
    public void addFriend(){
        try {
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            ps1.println("addfriend");
            ps1.println(DataHolder.getInstance().getData()); /// from user 
            ps1.println(friendsTable1.getSelectionModel().getSelectedItem().getFriend_email()); /// to user
                        ps1.println("allusers");
                        ps1.println(DataHolder.getInstance().getData());
                        Thread.sleep(100);
                        
                        byte[] vecBytes9 = new byte[is1.available()];
                        is1.read(vecBytes9);
                        System.out.print(vecBytes9);
                        ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes9);
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

        } catch (IOException ex) {
            Logger.getLogger(FriendsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            String user = DataHolder.getInstance().getData();
            RequestsVBox.getChildren().clear();
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            
            //////////////////////////////////////////////////
            Platform.runLater(new Runnable(){
                @Override
                public void run() {

                    try {
                        ps1.println("allusers");
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


///////////////////////////////////////////////////////////
                              FilteredList<userFriends> filteredData = new FilteredList<>(user_fr, b -> true);
                            FnameSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                                    filteredData.setPredicate(user -> {
                                            // If filter text is empty, display all persons.
                                            if (newValue == null || newValue.isEmpty()) {
                                                    return true;
                                            }
                                            // Compare first name and last name of every person with filter text.
                                            String lowerCaseFilter = newValue.toLowerCase();
                                            if (user.getFriend_fname().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                                                    return true; // Filter matches first name.
                                            } else if (user.getFriend_lname().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                                    return true; // Filter matches last name.
                                            }
                                            else if (String.valueOf(user.getFriend_email()).indexOf(lowerCaseFilter)!=-1)
                                                 return true;
                                                 else  
                                                     return false; // Does not match.
                                    });
                            }); 
                            SortedList<userFriends> sortedData = new SortedList<>(filteredData);
                            sortedData.comparatorProperty().bind(friendsTable1.comparatorProperty());
                            friendsTable1.setItems(sortedData);
                            
                        //////////////////////////////////////////////////

                        ps1.println("friendRequests");
                        ps1.println(user);
                        Thread.sleep(100);
                        byte[] vecBytes1 = new byte[is1.available()];
                        is1.read(vecBytes1);
                        System.out.print(vecBytes1);
                        ByteArrayInputStream bis1 = new ByteArrayInputStream(vecBytes1);
                        ObjectInputStream ois1 = new ObjectInputStream(bis1);
                        ArrayList<UserData> arr1;
                        arr1 = (ArrayList<UserData>)ois1.readObject();
                        System.out.println(arr1);
                        Image reqimg;
                        for (int i = 0; i < arr1.size(); i++){
                            if (arr1.get(i).getImg_url() == null ){
                                reqimg=new Image(getClass().getResourceAsStream("icons/user.png"));
                            }
                            else { reqimg=new Image(getClass().getResourceAsStream(arr1.get(i).getImg_url()));}
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("friendRequest.fxml"));
                            try {
                                HBox hBox= fxmlLoader.load();
                                friendRequestController cic = fxmlLoader.getController();
                                cic.setData(arr1.get(i).getFname(),arr1.get(i).getEmail(),reqimg);
                                RequestsVBox.getChildren().add(hBox);
                            }
                            catch (IOException e) {
                            }}                  } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
}});
            
        } 
         
        catch (IOException ex) {
            Logger.getLogger(ConnectPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }    

    

