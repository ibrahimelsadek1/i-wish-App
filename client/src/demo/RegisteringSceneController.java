/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import static demo.ItemsPaneController.generateUniqueFileName;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class RegisteringSceneController implements Initializable {

    @FXML
    private TextField fnameTF;
    @FXML
    private TextField lnameTF;
    @FXML
    private TextField emailTF;
    @FXML
    private TextField mobileTF;
    @FXML
    private PasswordField PasswordTF;
    @FXML
    private Button registerBT;
    @FXML
    private Hyperlink loginlink;

    private Parent root;
    private Stage stage;
    private Scene scene;
    
     File selectedFile;
     Image image;
     File output = null;
     
    static Socket socket;
    static DataInputStream dis ;
    static PrintStream ps ;
    @FXML
    private PasswordField PasswordTF2;
    @FXML
    private ImageView ProfImag;
    @FXML
    private Button uploadPP;
    
    @FXML
    public void signinSwitch(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("loginScene1.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
       @FXML
   public void handleUpload() throws FileNotFoundException, IOException{              
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image");
                fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                selectedFile = fileChooser.showOpenDialog(uploadPP.getScene().getWindow());
                if (selectedFile != null) {
  
                            // Read the image file into a byte array
                            FileInputStream fis = new FileInputStream(selectedFile);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                baos.write(buffer, 0, bytesRead);
                            }
                            fis.close();
                            byte[] imageBytes = baos.toByteArray();
                            // Set the image view to display the selected image
                            image = new Image(selectedFile.toURI().toString());
                            ProfImag.setImage(image);
                        }     }
    
    @FXML
    String new_name;
    public void register(ActionEvent event) throws IOException{
        if (selectedFile == null){
             new_name="icons/user2.png";
            }
        else{
            new_name = "icons/"+generateUniqueFileName()+".png";
              Files.copy(selectedFile.toPath(), new File("C:\\Users\\ibrahim\\Documents\\NetBeansProjects\\demo\\src\\demo\\" +new_name).toPath());}
        try {
                socket=new Socket("127.0.0.1", 5005);
                dis = new DataInputStream(socket.getInputStream());
                ps = new PrintStream(socket.getOutputStream()); 
                
     new Thread(){
                  public void run(){
                         try {  
                        // Validate input fields
                        if(fnameTF.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"First Name is mandatory.");
                            return;
                        }
                        if(lnameTF.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"Last Name is mandatory.");
                            return;
                        }
                        if(emailTF.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"Email is mandatory.");
                            return;
                        } else if(!emailTF.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")){
                            JOptionPane.showMessageDialog(null,"Please enter a valid email address.");
                            return;
                        }
                        if(mobileTF.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"Mobile is mandatory.");
                            return;
                        } else if(!mobileTF.getText().matches("\\d{11}")){
                            JOptionPane.showMessageDialog(null,"Please enter a valid 11-digit mobile number.");
                            return;
                        }
                        if(PasswordTF.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"Please enter password.");
                            return;
                        }
                        if(PasswordTF2.getText().isEmpty()){
                            JOptionPane.showMessageDialog(null,"Please re-enter the password.");
                            return;
                        }
                        if(!PasswordTF.getText().equals(PasswordTF2.getText())){
                            JOptionPane.showMessageDialog(null,"Passwords do not match.");
                            return;
                        }

                        // Send registration request to server
                        ps.println("registerReq");
                        ps.println(fnameTF.getText());
                        ps.println(lnameTF.getText());
                        ps.println(emailTF.getText());
                        ps.println(mobileTF.getText());
                        ps.println(PasswordTF.getText());
                        ps.println(new_name);

                        // Wait for response from server
                        while (true){ 
                            String response = dis.readLine();
                            if (response.equals("registerSuccess")){
                                System.out.println(response);
                                    fnameTF.clear();
                                    lnameTF.clear();
                                    emailTF.clear();
                                    mobileTF.clear();
                                    PasswordTF.clear();
                                    PasswordTF2.clear();
                                    ProfImag.setImage(null);
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        alert.setTitle("Registration");
                                        alert.setHeaderText("Congratulations");
                                        String s = "Registration has been completed successfully. You can now login.";
                                        alert.setContentText(s);
                                        alert.show();
                                    }
                                });
                                break;
                            } else if (response.equals("ExistedUser")){
                                System.out.println(response);
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        Alert alert = new Alert(AlertType.INFORMATION);
                                        alert.setTitle("Registration");
                                        alert.setHeaderText("User Already Exists");
                                        String s = "The email you are using for registration is already associated with an existing account.";
                                        alert.setContentText(s);
                                        alert.show();
                                    }
                                });
                                break;
                            }
                        }
                    } catch (SocketException se) { 
                        System.out.println("no con");      
                    } catch (NullPointerException ne) {
                        System.out.println("iiii");
                    } catch (IOException ex) {
                        System.out.println("nuio");
                    }
                         finally{ try {
                             ps.close();
                             dis.close();
                             socket.close();      } catch (IOException ex) {
                                 Logger.getLogger(RegisteringSceneController.class.getName()).log(Level.SEVERE, null, ex);
                             }
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
                        String s ="Please tru again";
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
