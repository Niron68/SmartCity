package smartcity.ui;
import info.util.javafx.FXUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
/**
 * Vous pouvez décrire votre classe ModalResultat ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public class ModalResultat {
    @FXML
    private Stage stage;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Button newGameButton;
    
    @FXML
    private Button exitButton;
    
     private boolean addOnSave = false;
    
    private SmartCityApplication application;
    
    public ModalResultat(SmartCityApplication application) {
        this.application = application;
    }
    
    @FXML
    public void onCancel() {
        this.stage.close();
    }
    
    @FXML
    public void onWindowShown() {
        this.stage.setResizable(false);
    }
    
    @FXML
    public boolean onWindowCloseRequest() {
        return true;
    }
}
