package smartcity.ui;
import java.io.IOException;

import info.util.javafx.FXUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Vous pouvez décrire votre classe MenuPrincipal ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public class ModalMenuPrincipal {
    @FXML
    private Stage stage;
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button playButton;

    @FXML 
    private Slider citySizeSlider;
    @FXML 
    private Label citySizeLabel;
    
    @FXML
    private TextField nameCityField;
    
    @FXML
    private Button configurerEquipeUnButton;
    
    @FXML 
    private Button configurerEquipeDeuxButton;
    
    private SmartCityApplication application;
    private Equipe equipe1;
    private Equipe equipe2;
    
    public ModalMenuPrincipal(SmartCityApplication application) {
        this.application = application;
        
        this.equipe1 = new Equipe("Équipe A");
        this.equipe2 = new Equipe("Équipe B");
    }
    
    @FXML
    public void initialize() {        
        this.citySizeLabel.textProperty().bind(this.citySizeSlider.valueProperty().asString("%.0f"));
    }
    
    @FXML
    public void onCancel() {
        this.stage.close();
    }
    
    public int getSize() {
    	return this.citySizeSlider.valueProperty().intValue();
    }
    
    public boolean validate() throws Exception {
        if(this.nameCityField.getText().length() == 0)
            throw new Exception("Nom invalide !");        
        return true;
    }

   
    @FXML
    public void onWindowShown() {
        this.stage.setResizable(false);
    }
    
    @FXML
    public boolean onWindowCloseRequest() {
        return true;
    }
    
    @FXML
    public void onConfigurerEquipe1Button() {
    	this.showEquipeModal(equipe1);
    }
    
    @FXML
    public void onConfigurerEquipe2Button() {
    	this.showEquipeModal(equipe2);
    }
    
    private void showEquipeModal(Equipe equipe) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionEquipe.fxml"), application.getBundle());
    		ModalGestionEquipe controller = new ModalGestionEquipe(application, equipe);
    		loader.setController(controller);
    		FXUtil.associate(loader.getController(), application.getPrimaryStage());
    		
    		Parent modal = loader.load();
    		Stage modalStage = new Stage();
    		FXUtil.associate(controller, modalStage);
    		modalStage.setScene(new Scene(modal));
    		modalStage.initModality(Modality.APPLICATION_MODAL);
    		modalStage.setTitle(application.getBundle().getString("titre.menu"));
    		
    		modalStage.show();
    	} catch(Exception e) {
    		System.out.println(e.getMessage());
    		System.out.println(e.getCause());
    		e.printStackTrace();
    	}
    }
    
    @FXML
    public void onStartGame() throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Partie.fxml"), application.getBundle());
    	loader.setController(new PartieController(this.getSize(), this.nameCityField.getText(), equipe1, equipe2));
    	FXUtil.associate(loader.getController(), application.getPrimaryStage());
    	
    	Parent root = loader.load();
    	this.application.getPrimaryStage().setScene(new Scene(root));
    	
    	if(this.nameCityField.getText().length() != 0)
    		this.application.getPrimaryStage().setTitle("SmartCity : " + this.nameCityField.getText());
    }

}
