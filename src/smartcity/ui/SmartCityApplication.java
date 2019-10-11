package smartcity.ui;

import java.util.Locale;
import java.util.ResourceBundle;
import info.util.javafx.FXUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Vous pouvez décrire votre classe Application ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public class SmartCityApplication extends Application {
    private ResourceBundle bundle;
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	this.primaryStage = primaryStage;
        this.bundle = ResourceBundle.getBundle("smartcity/ui/SmartCity");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuPrincipal.fxml"), bundle);
            loader.setController(new ModalMenuPrincipal(this));
            Parent root = loader.load();

            FXUtil.associate(loader.getController(), primaryStage);
            primaryStage.setTitle(bundle.getString("titre.menu"));
            primaryStage.setScene(new Scene(root));


            primaryStage.show();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
    
    public static void main(String[] args) {
        // Locale.setDefault(Locale.ENGLISH);
        
        Application.launch(args);
    }
    
    public ResourceBundle getBundle() {
		return bundle;
	}

    public Stage getPrimaryStage() {
    	return this.primaryStage;
    }

}
