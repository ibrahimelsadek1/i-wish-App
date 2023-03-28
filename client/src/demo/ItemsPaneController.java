/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang.RandomStringUtils;
import ser.UserItems;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class ItemsPaneController implements Initializable {

    @FXML
    private ScrollPane myitemspane;

    @FXML
    private TextField prodNameLabel;
    @FXML
    private TextField PriceLabe;
    @FXML
    private ChoiceBox<String> categorylist;
    @FXML
    private Button uploadeBT;
    @FXML
    private Button SubmitBT;
    
    private final String[] categories = {"Cloths","Electronics","Gifts","Sports","Games","Beauty","Health","Other"};
    @FXML
    private HBox itemsHBox;
    
    static Socket socket1;
    static DataInputStream dis1 ;
    static PrintStream ps1 ;
    
     static   Socket socket2;
     static   PrintStream ps2;
    @FXML
    private ImageView item_img;
    
    File selectedFile;
    Image image;
    File output = null;
          
    
    //////////////////////////////////////////////
          
    @FXML
   public void handleUpload() throws FileNotFoundException, IOException{
        
              
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Image");
                fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                selectedFile = fileChooser.showOpenDialog(uploadeBT.getScene().getWindow());
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
                            item_img.setImage(image);
                            
                        }
                            
                        }
      
    /////////////////////////////////////////////
    @FXML
   public void submit() throws IOException{
       String new_name;
                    if (selectedFile == null) {
                        new_name="";
                    }
                    else {
                         new_name = "icons/"+generateUniqueFileName()+".png";
                         Files.copy(selectedFile.toPath(), new File("C:\\Users\\ibrahim\\Documents\\NetBeansProjects\\demo\\src\\demo\\" +new_name).toPath());

                    }
           
                    String data = DataHolder.getInstance().getData();
                    if (data == null || data.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"please restart the program");
                        return;
                    }

                    String name = prodNameLabel.getText();
                    if (name == null || name.isEmpty() || !name.matches("[a-zA-Z0-9\\s]+")) {
                        JOptionPane.showMessageDialog(null,"please enter item name");
                        return;
                    }

                    String price = PriceLabe.getText();
                    if (price == null || price.isEmpty() || !price.matches("\\d+(\\.\\d+)?")) {
                        JOptionPane.showMessageDialog(null,"please enter a valid price (numbers only)");
                        return;
                    }

                    String category = categorylist.getSelectionModel().getSelectedItem();
                    if (category == null || category.isEmpty()) {
                        JOptionPane.showMessageDialog(null,"please select category");
                        return;
    
                    }
                
        try {
             socket2=new Socket("127.0.0.1", 5005);
             ps2 = new PrintStream(socket2.getOutputStream());



                    // If all inputs are valid, send the data
                    ps2.println("submitItem");
                    ps2.println(data);
                    ps2.println(name);
                    ps2.println(price);
                    ps2.println(category);

                    ps2.println(new_name); /// url
                    prodNameLabel.clear();
                    PriceLabe.clear();
                    categorylist.valueProperty().set(null);
                    item_img.setImage(null);
                    System.out.println("Image saved successfully.");
 
        } catch (IOException ex) {
            Logger.getLogger(ItemsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ps2.close();
            socket2.close();
        }

        } 
   //////////////////////////////////
    /**
     * Initializes the controller class.
     */
   
    public static String generateUniqueFileName() {
            String filename = "";
            long millis = System.currentTimeMillis();
            String datetime = new Date().toGMTString();
            datetime = datetime.replace(" ", "");
            datetime = datetime.replace(":", "");
            String rndchars = RandomStringUtils.randomAlphanumeric(8);
            filename = rndchars + "_" + datetime + "_" + millis;
            return filename;
    }
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            categorylist.getItems().addAll(categories);
            String user = DataHolder.getInstance().getData();
            itemsHBox.getChildren().clear();
            
            socket1=new Socket("127.0.0.1", 5005);
            ps1 = new PrintStream(socket1.getOutputStream());
            InputStream is1 = socket1.getInputStream();
            
            ps1.println("allitems");
            ps1.println(user);
            Thread.sleep(100);
            byte[] vecBytes = new byte[is1.available()];
            is1.read(vecBytes);
            System.out.print(vecBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            ArrayList<UserItems> arr=null;
            arr = (ArrayList<UserItems>)ois.readObject();
            System.out.println(arr);
            
            Image wishImg = null;
            for (int i = 0; i < arr.size(); i++){
                if (arr.get(i).getImg_url() == null ){
                    wishImg=new Image(getClass().getResourceAsStream("icons/items.png"));
                }
                else { System.out.println(arr.get(i).getImg_url());
                    wishImg=new Image(getClass().getResourceAsStream(arr.get(i).getImg_url()));
//                Thread.sleep(100);
                
                }

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("MarketItem.fxml"));
                try {
                    VBox vBox= fxmlLoader.load();
                    MarketItemController cic = fxmlLoader.getController();
//                    System.out.println(arr.get(i).getItem_name()+arr.get(i).getPrice()+arr.get(i).getCurr_cont());
                    cic.setData(arr.get(i).getItem_id(),arr.get(i).getItem_name(),arr.get(i).getPrice(),arr.get(i).getCat(),wishImg);
                    itemsHBox.getChildren().add(vBox);
                }
                catch (IOException e) {
}
                catch (NullPointerException ne) {
                    return;}
            }
        } catch (IOException ex) {
            Logger.getLogger(ItemsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ItemsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ItemsPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
}
       
    

