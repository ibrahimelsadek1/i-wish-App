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
import ser.ContributerClass;

/**
 * FXML Controller class
 *
 * @author ibrahim
 */
public class ContributersSceneController implements Initializable {

    @FXML
    private TableView<ContributerClass> tabview;
    @FXML
    private TableColumn<ContributerClass, String> emailcol;
    @FXML
    private TableColumn<ContributerClass, String> totalcontCol;
    @FXML
    private TableColumn<ContributerClass, String> colname;
    
            
    static Socket socket1;
    static DataInputStream dis1 ;
    InputStream is1;
    static PrintStream ps1 ;

    /**
     * Initializes the controller class.
     */
                        

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                        try {
                                    socket1=new Socket("127.0.0.1", 5005);
                                    ps1 = new PrintStream(socket1.getOutputStream());
                                    is1 = socket1.getInputStream();
                                    String user = DataHolder.getInstance().getData();
                                    String item_id = DataHolder.getInstance().getItem_id();

                                    ps1.println("contributers");
                                    ps1.println(user);
                                    ps1.println(item_id);
                                    Thread.sleep(100);
                                    byte[] vecBytes1 = new byte[is1.available()];
                                    is1.read(vecBytes1);
                                    System.out.print(vecBytes1);
                                    ByteArrayInputStream bis = new ByteArrayInputStream(vecBytes1);
                                    ObjectInputStream ois = new ObjectInputStream(bis);
                                    ArrayList<ContributerClass> arr5;
                                    arr5 = (ArrayList<ContributerClass>)ois.readObject();
                                    ObservableList<ContributerClass> contData = FXCollections.observableArrayList();
                                    for (int i = 0; i < arr5.size(); i++){
                                        contData.add(arr5.get(i));
                                        System.out.print(arr5.get(i).getFirstname());
                                    }   
                                    emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
                                    colname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
                                    totalcontCol.setCellValueFactory(new PropertyValueFactory<>("total_cont"));
                                    tabview.setItems(contData);
                            } catch (   IOException | InterruptedException | ClassNotFoundException ex) {
                                Logger.getLogger(ContributersSceneController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                             finally{
                                    try {
                                        is1.close();
                                        ps1.close();
                                        socket1.close();
                                    } catch (IOException ex) {
                                        Logger.getLogger(ContributersSceneController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                             }
                
                
    }});

    }    
    
}
