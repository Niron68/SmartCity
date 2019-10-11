package smartcity.ui;

import java.util.Random;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import smartcity.robot.Kind;

/**
 * Vous pouvez décrire votre classe Equipe ici
 * 
 * @author  Indiquez votre nom
 * @version Indiquez la date
 */
public class Equipe {
    private SimpleStringProperty nom = new SimpleStringProperty(this, "name", "Équipe"); 
    private ObservableList<Kind> kinds = FXCollections.observableArrayList();
    private SimpleDoubleProperty size = new SimpleDoubleProperty(this, "size", 4);
    
    public Equipe() {
    	this("Équipe");
    }
    
    public Equipe(String nom) {
    	this.nom.set(nom);
    	Random r = new Random();
    	for(int i = 0; i < 6; i++)
    		this.kinds.add(Kind.values()[r.nextInt(Kind.values().length)]);
    }

	public SimpleStringProperty nomProperty() {
		return nom;
	}

	public SimpleDoubleProperty sizeProperty() {
		return size;
	}
	
	public ObservableList<Kind> getKinds() {
		return this.kinds;
	}
	
	public Kind[] getKindsArray() {
		Kind[] kinds = new Kind[(int) size.get()];
		for(int i = 0; i < kinds.length; i++)
			kinds[i] = this.kinds.get(i);
		return kinds;
	}
}
