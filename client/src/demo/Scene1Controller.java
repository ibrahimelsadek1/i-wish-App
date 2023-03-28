/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class Scene1Controller implements Initializable {

    @FXML
    private TextField usernameTF;
    @FXML
    private Button loginbt;
    @FXML
    private PasswordField paswordTF;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Hyperlink signuplink;
    
    private Parent root;
    private Stage stage;
    private Scene scene;
    String resopnse;
    
    static Socket socket;
    static DataInputStream dis ;
    static PrintStream ps ;
    @FXML
    private Text wronglogin;
    
    @FXML
    public void clearMsg(){
        wronglogin.setText("");
    }
    
    @FXML
    public void signupSceneSwitch(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("registeringScene.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void login(ActionEvent event)  {
        
        try {
            socket=new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            new Thread(){
                public void run(){
                    try {   ps.println("loginReq");
                    ps.println(usernameTF.getText());
                    ps.println(paswordTF.getText());
                    while (true){
                        String response=dis.readLine();
                        if (response.equals("loginSuccess")){
                            System.out.println(response);
                            Platform.runLater(new Runnable(){
                                @Override
                                public void run() {
                                    try {
                                            DataHolder.getInstance().setData(usernameTF.getText());
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
                                            root= loader.load();
                                            stage =(Stage)((Node)event.getSource()).getScene().getWindow();
                                            scene=new Scene(root);
                                            stage.setScene(scene);
                                            stage.show();     }
                                    catch (IOException ex) {
                                        Logger.getLogger(Scene1Controller.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                        } else{  wronglogin.setText("Wrong Email or Password");  }
                    }}
                    catch (SocketException se) {
                        System.out.println("no con");      
                    }
                    catch (NullPointerException ne) {
                        System.out.println("iiii");
                    }
                    catch (IOException ex) {
                        System.out.println("nuio");
                    }
                }
            }.start();
        } 
        catch (ConnectException conex) {
                 Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Connection Problem");
                        alert.setHeaderText("Server not responding");
                        String s ="Please try again";
                        alert.setContentText(s);
                        alert.show();
                         }
                    });
        }
        catch (IOException ex) {
            Logger.getLogger(Scene1Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
        
        
    }    
    
}
