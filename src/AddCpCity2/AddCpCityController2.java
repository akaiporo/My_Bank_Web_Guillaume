package AddCpCity2;

import javax.persistence.EntityManager;

import application.ControllerBase;
import application.Mediator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import metier.CpCity;

public class AddCpCityController2 extends ControllerBase { 
	private EntityManager em;
	CpCity currentCpCity=new CpCity();
	
	@FXML private TextField postal_code;
	@FXML private TextField city;
	@FXML private Label cpcity_error;
	@FXML private Button OK;
	@FXML private Button cancel;
	
	@Override
	
	/**
	 * Initialisation des messages d'erreur!
	 */
	
	public void initialize(Mediator mediator) {
		em = mediator.createEntityManager();
		cpcity_error.setText("");
	}
	/**
	 * Pour créer un owner avec des nouveaux postalcode et city:
	 * @param event : Création d'un evénement avec le bouton ok pour rajouter une ville et/ ou un code postal
	 * En testant à chaque fois les differents paramètres/champs;
	 * Puis renvoie vers la page de création de owner/user quand la ville et/oule code postal est rentré!
	 */
	@FXML
	private void handleButtonOK (ActionEvent event){
		try{
			currentCpCity.setPostalCode(postal_code.getText());
		}
		catch (IllegalArgumentException e){
			cpcity_error.setText(e.getMessage());
		}
		try{
			currentCpCity.setCity(city.getText());
		}
		catch (IllegalArgumentException e){
			cpcity_error.setText(e.getMessage());
		}
		
		em.getTransaction().begin();
		em.persist(currentCpCity);
		try{
			em.getTransaction().commit();
		}
		catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
		}
		this.loadSubScene("../AddUser/AddUserView.fxml");		
	}
	/**
	 * Ce bouton renvoie vers la page de création de owner/user si on ne veut plus rajouter!
	 * @param event : évènement de l'utilisateur
	 */
	@FXML
	private void handleButtonCancel (ActionEvent event){
		this.loadSubScene("../AddUser/AddUserView.fxml");
	}
}
