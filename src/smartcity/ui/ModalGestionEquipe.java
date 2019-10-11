package smartcity.ui;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import smartcity.robot.Kind; 

/**
 * Vous pouvez décrire votre classe GestionEquipe ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public class ModalGestionEquipe {
    @FXML
    private Stage stage;
    
    @FXML
    private TextField nameTeamField;
    
    @FXML
    private Slider teamSizeSlider;
    
    @FXML 
    private Label teamSizeLabel;
    
    @FXML
    private Button validateButton;
    
    @FXML
    private GridPane robotsGrid;

    private Equipe equipe;

	private ArrayList<ChoiceBox<Kind>> choiceboxes = new ArrayList<>(6);

	private SmartCityApplication application;

    public ModalGestionEquipe(SmartCityApplication application, Equipe equipe) {
    	this.application = application;
        this.equipe = equipe;
    }

    @FXML
    public void initialize() {
        this.nameTeamField.textProperty().bindBidirectional(equipe.nomProperty());
        this.teamSizeSlider.valueProperty().bindBidirectional(equipe.sizeProperty());
        this.teamSizeLabel.textProperty().bind(this.teamSizeSlider.valueProperty().asString("%.0f"));
        
        for(int i = 0; i < 6; i++) {
        	ChoiceBox<Kind> cb = new ChoiceBox<Kind>(FXCollections.observableArrayList(Kind.values()));
        	cb.setPrefWidth(200);
        	cb.converterProperty().set(new StringConverter<Kind>() {
				@Override
				public Kind fromString(String arg0) {
					return Kind.valueOf(arg0);
				}

				@Override
				public String toString(Kind arg0) {
					return application.getBundle().getString("equipe." + arg0.getType());
				}
        	});
        	cb.setValue(equipe.getKinds().get(i));
        	choiceboxes.add(cb);
        	final int index = i;
        	cb.valueProperty().addListener((e) -> {
        		equipe.getKinds().set(index, cb.getValue());
        	});
    		this.robotsGrid.add(cb, i % 3, i / 3 * 3);
        }

        this.equipe.sizeProperty().addListener((prop) -> updateCheckboxesVisibility());
        updateCheckboxesVisibility();
    }
    
    private void updateCheckboxesVisibility() {
    	for(int i = 0; i < equipe.sizeProperty().getValue(); i++)
    		choiceboxes.get(i).setVisible(true);
    	for(double i = equipe.sizeProperty().getValue(); i < choiceboxes.size(); i++)
    		choiceboxes.get((int) i).setVisible(false);
    }
   
    @FXML
    public void onWindowShown() {
        this.stage.setResizable(false);
    }
    
    @FXML
    public void onValidate() {
    	this.stage.close();
    }
}
