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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import ser.Notification;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class NotificationPaneController implements Initializable {

    private Text emailHolder;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    static InputStream is1;

    @FXML
    private TableView<Notification> friendsTable1;

    @FXML
    private TableColumn<Notification, String> email1;

    /**
     * Initializes the controller class.
     */
    public void setData(String email){
    emailHolder.setText(email);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            
            //////////////////////////////////////////////////
            Platform.runLater(new Runnable(){
                @Override
                public void run() {

                    try {
                        ps1.println("allNotifications");
                        ps1.println(DataHolder.getInstance().getData());
                        Thread.sleep(100);
                        
                        byte[] vecBytes = new byte[is1.available()];
                        is1.read(vecBytes);
                        System.out.print(vecBytes);
                        ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        ArrayList<Notification> arr5;
                        arr5 = (ArrayList<Notification>)ois.readObject();
                        ObservableList<Notification> user_noti = FXCollections.observableArrayList();
                        for (int i = 0; i < arr5.size(); i++){
                            user_noti.add(arr5.get(i));
                        }
                        email1.setCellValueFactory(new PropertyValueFactory<>("notification"));
                        friendsTable1.setItems(user_noti);
                        }
        
                        catch (IOException ex) {
                            Logger.getLogger(NotificationPaneController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                        Logger.getLogger(NotificationPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(NotificationPaneController.class.getName()).log(Level.SEVERE, null, ex);
                    }
      
            }
                    });
    }   catch (IOException ex) {
            Logger.getLogger(NotificationPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
   

    

