/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class MainSceneController implements Initializable {
    
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private Hyperlink logoutBT;
    private Label namelabel;
    private Label mobileLabel;
    private ImageView perimg;
    
    String resopnse;
    String email="nooooo";
    
    static Socket socket;
    static DataInputStream dis ;
    static PrintStream ps ;
    @FXML
    private Text emailHolder;
    @FXML
    private Button friendsBTN;
    @FXML
    private AnchorPane pagepane;
    @FXML
    private BorderPane main;
    @FXML
    private Text notification;
    
    private ScheduledExecutorService executor;
    @FXML
    private Pane NotificationPane;
    @FXML
    private AnchorPane headerPane;
    @FXML
    private ImageView icImg;
      


    
    @FXML
    public void logoutSwitch(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("loginScene1.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void setData(String fname, String email,String mobile, String imgURL){
        Image itemImg=new Image(getClass().getResourceAsStream(imgURL));
        perimg.setImage(itemImg);
        namelabel.setText(fname);
        mobileLabel.setText(String.valueOf(mobile));  
    }
    
    @FXML
    public void loadPageProfile() throws IOException{
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("profilepane.fxml"));
                Parent root= fxmlLoader.load();
                main.setCenter(root);                           
    }
    
    @FXML
    public void loadFriends() throws IOException{
                root=null;
                FXMLLoader fxmlLoader1 = new FXMLLoader();
                fxmlLoader1.setLocation(getClass().getResource("friendsPane.fxml"));
                root= fxmlLoader1.load();
                main.setCenter(root);    
    }
    @FXML
    public void loadItemsPage() throws IOException{
                root=null;
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("itemsPane.fxml"));
                root= fxmlLoader.load();
                main.setCenter(root);    
    }
    @FXML
    public void loadConnectPage() throws IOException{
                root=null;
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ConnectPane.fxml"));
                root= fxmlLoader.load();
                main.setCenter(root);    
    }
    
     public void notificationPage() throws IOException{
                root=null;
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("NotificationPane.fxml"));
                root= fxmlLoader.load();
                main.setCenter(root);    
    }
    
    private void showNotification(String message) {
        if (!message.equals("NoN")) {
            notification.setText(message);
            NotificationPane.getChildren().setAll(notification,icImg);
            NotificationPane.setVisible(true);
        } else {
            NotificationPane.setVisible(false);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String user = DataHolder.getInstance().getData();
            emailHolder.setText(user);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("profilepane.fxml"));
            Parent root= fxmlLoader.load();
            main.setCenter(root); 
        } catch (IOException ex) {  
            Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                socket=new Socket("127.0.0.1", 5005);
                dis = new DataInputStream(socket.getInputStream());
                ps = new PrintStream(socket.getOutputStream());
                
                ps.println("getNotification");
                ps.println(DataHolder.getInstance().getData());
                String response = dis.readLine();
                System.out.println(response);
                Platform.runLater(() -> showNotification(response));
            } catch (IOException ex) {
                Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally{
                try {
                    ps.close();
                    socket.close();
                    dis.close();       
                } catch (IOException ex) {
                    Logger.getLogger(MainSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0, 10 , TimeUnit.SECONDS);
   
    }    
}
