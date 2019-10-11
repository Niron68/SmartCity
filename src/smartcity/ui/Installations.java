package smartcity.ui;
import javafx.scene.paint.Color;
/**
 * Vous pouvez décrire votre classe Installations ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public enum Installations {
    BATIMENT_NORMAL(0,Color.BISQUE), ROUTE(1,Color.CORAL), BORNE_DE_RECHANGE(2,Color.CHARTREUSE), MAGASIN(3,Color.CRIMSON), ENTREPOT(4,Color.DARKSEAGREEN),STOCKAGE(5,Color.DEEPPINK);
    
    private final int installation;
    
    private Installations(int num, Color col) {
        this.installation = num;
    }   

}
