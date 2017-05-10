package AddUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;

import application.ControllerBase;
import application.Mediator;
import application.Tools;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import metier.Owner;
import metier.Address;
import metier.Advisor;
import metier.CpCity;

public class AddUserController extends ControllerBase { 
	private EntityManager em;
	private Owner owner = new Owner();
	private CpCity cpcity = new CpCity();
	private Address address = new Address();
	List<String> cities = new ArrayList<String>();

	
	@FXML private TextField login;
	@FXML private TextField pwd;
	@FXML private TextField confirm_pwd;
	@FXML private TextField owner_name;
	@FXML private TextField owner_firstname;
	@FXML private TextField owner_mail;
	@FXML private TextField owner_id_address1;
	@FXML private TextField owner_id_address2;
	@FXML private ChoiceBox <String> postalcode;
	@FXML private ChoiceBox <String> city;
	@FXML private TextField owner_phonenumber;
	@FXML private DatePicker owner_birthdate;
	@FXML private Button btn_ok;
	@FXML private Button btn_cancel;
	
	@FXML private Label errlogin;
	@FXML private Label errpwd;
	@FXML private Label errconfirmpwd;
	@FXML private Label errname;
	@FXML private Label errfirstname;
	@FXML private Label errmail;
	@FXML private Label erraddress1;
	@FXML private Label erraddress2;
	@FXML private Label errpostalcode;
	@FXML private Label errcity;
	@FXML private Label errphonenumber;
	@FXML private Label errbirthdate;
	
	@Override
	/**
	 * Initialisation des ChoiceBox "city et postalcode", puis des messages d'erreur!
	 */
	
	public void initialize(Mediator mediator) {
		
		errlogin.setText("");
		errpwd.setText("");
		errconfirmpwd.setText("");
		errname.setText("");
		errfirstname.setText("");
		errmail.setText("");
		erraddress1.setText("");
		erraddress2.setText("");
		errpostalcode.setText("");
		errcity.setText("");
		errphonenumber.setText("");
		errbirthdate.setText("");
		
		try {	
			em = mediator.createEntityManager();
			
			cities.add("(new city)");
			this.city.setItems(FXCollections.observableList(cities));
			
			List<String> postalcodes = em.createNamedQuery("cpcity.findAllpostalcode", String.class).getResultList();
			postalcodes.add("(new postalcode)");
			this.postalcode.setItems(FXCollections.observableList(postalcodes));
		}
		catch(PersistenceException e) {
			this.processPersistenceException(e);
		}
	}
	/** 
	 * @param event :On va crï¿½er un ï¿½vï¿½nement newCity pour pouvoir rajouter une ville 
	 *  si elle n'existe pas dï¿½jï¿½ dans la BDD
	 */
	@FXML
	private void newCity (ActionEvent event) {
		ChoiceBox catCity  = (ChoiceBox)event.getTarget();
		String tmp = (String) catCity.getValue();
		if(tmp.equals("(new city)")) {
			this.loadSubScene("../AddCpCity2/AddCpCityView2.fxml");
		}
	}
	/**
	 * @param event :On va crï¿½er un ï¿½vï¿½nement handlePostalcode pour pouvoir rajouter un code postal 
	 *  s'l n'existe pas dï¿½jï¿½ dans la BDD
	 */
	@FXML 
	private void handlePostalcode (ActionEvent event) {
		
		ChoiceBox catPostalcode  = (ChoiceBox)event.getTarget();
		String tmp = (String)catPostalcode.getValue();
		if(tmp.equals("(new postalcode)")) {
			this.loadSubScene("../AddCpCity2/AddCpCityView2.fxml");
		}
		else {
			city.getItems().removeAll(cities);
			Query q = em.createQuery("SELECT c.city FROM CpCity c WHERE c.postalCode = :postalcode",String.class); 
			q.setParameter("postalcode", postalcode.getValue());
			cities = q.getResultList();
			cities.add("(new city)");
			this.city.setItems(FXCollections.observableList(cities));
		}
	}
	/**
	 * Création d'un evénement avec le bouton ok pour rajouter(set) un owner dans la BDD
	 * En testant à chaque fois les differents paramètres/champs;
	 * Dans l'idéal, il aurait aussi fallu tester l'égalité des champs pwd et confirm_pwd;
	 * Puis renvoyer une erreur si les deux champs ne sont pas identiques
	 * Après l'ajout on est redirigé vers la page d'authentification
	 * @param Event : 
	 */
	@FXML
	private void handleButtonOk(ActionEvent Event) {
		/*
		 * L'objet <CpCity> ("cpcity",crÃ©Ã© vide) est reliÃ© Ã  un cpcity de la base via une query 
		 */
		
		Query z = em.createQuery("SELECT p FROM CpCity p WHERE p.postalCode =:postalcode AND p.city =:city", CpCity.class);
		if (postalcode.getValue()!=null && city.getValue()!=null){
			z.setParameter("postalcode", postalcode.getValue());
			z.setParameter("city", city.getValue());
		}
		else {
			errpostalcode.setText("Please choose an existing postal code or add one");
			errcity.setText("Please choose an existing city or add one");
			return;
		}
		
		try {
			cpcity=(CpCity)z.getSingleResult();
		}
		catch (NoResultException err){
			return;
		}
		
		/*
		 * L'objet <Address> ("address",crÃ©Ã© vide) est retrouvÃ© dans la base ou rempli via les setters en testant chaque champ
		 */
		
		Query q = em.createQuery("SELECT a FROM Address a WHERE a.line1 = :line1 AND a.line2 =:line2 AND a.cpCity =:cpcity", Address.class);
		q.setParameter("line1", owner_id_address1.getText());
		q.setParameter("line2", owner_id_address2.getText());
		q.setParameter("cpcity", cpcity);
		try {
				address=(Address)q.getSingleResult(); //si adresse dÃ©jÃ  existante
		}
		catch (NoResultException err) {
			try {
				address.setLine1(owner_id_address1.getText());
			}
			catch(IllegalArgumentException e){
				erraddress1.setText(e.getMessage());
				return;
			}
			address.setLine2(owner_id_address2.getText()); // L'address2 peut ï¿½tre vide
			try {
				address.setCpCity(cpcity);
			}
			catch(NullPointerException e){	
				return;
			}
			/*
			 * Si l'adresse n'existe pas dÃ©jÃ  elle est commit dans la base
			 */
			em.getTransaction().begin();
			em.persist(address);
			try{
				em.getTransaction().commit();
			}
			catch(Exception e) {
				em.getTransaction().rollback();
				return;
			}
		}
		/*
		 * L'objet <Owner> ("owner",crÃ©Ã© vide) est rempli via les setters en testant chaque champ 
		 */
		
		try {
			owner.setLogin(login.getText());
		}
		catch  (IllegalArgumentException e) {
			errlogin.setText(" The login cannot be empty");
			return;
		}
		catch (NullPointerException e) {
			errlogin.setText(" The login cannot be null");
			return;
		}
		try {
			owner.setPwd(BCrypt.hashpw(pwd.getText(), BCrypt.gensalt(12)));
		}
		catch  (IllegalArgumentException e) {
			errpwd.setText(" The password cannot be empty");
			return;
		}
		catch (NullPointerException e) {
			errpwd.setText(" The password cannot be null");
			return;
		}
		
		try {
			owner.setName(owner_name.getText());
		}
		catch  (IllegalArgumentException e) {
			errname.setText(" The owner name cannot be empty");
			return;
		}
		catch  (NullPointerException e) {
			errname.setText(" The owner name cannot be null");
			return;
		}
		try {
			owner.setFirstName(owner_firstname.getText());
		}
		catch  (IllegalArgumentException e) {
			errfirstname.setText(" The owner firstname cannot be empty");
			return;
		}
		catch  (NullPointerException e) {
			errfirstname.setText(" The owner firstname cannot be null");
			return;
		}
		try {
			owner.setMail(owner_mail.getText());
		}
		catch  (IllegalArgumentException e) {
			errmail.setText(" The owner email cannot be empty");
			return;
		}
		try {
			owner.setPhoneNumber(owner_phonenumber.getText());
		}
		catch  (IllegalArgumentException e) {
			errphonenumber.setText(" The owner phonenumber cannot be empty");
			return;
		}
		try {
			owner.setBirthdate(Tools.LocalDateToDate(owner_birthdate.getValue()));
		}
		catch  (IllegalArgumentException e) {
			errbirthdate.setText(" The owner birthdate cannot be empty");
			return;
		}
		catch (NullPointerException e) {
			errbirthdate.setText(" The birthdate cannot be null");
			return;
		}
		try {
			owner.setAddress(address);
		}
		catch(NullPointerException e){	
			return;
		}
		
		/*
		 * Ajout d'un nouvel utilisateur dans la BDD
		 */
		
		em.getTransaction().begin();
		em.persist(owner);
		try{
			em.getTransaction().commit();
		}
		catch(Exception e) {
			em.getTransaction().rollback();
			return;
		} 
		 
		/*
		 * Chargement de la page d'authentification de l'application
		 */
		this.loadSubScene("../authentification/AuthentificationView.fxml");	
		
	}
	/**L'évènement du bouton cancel va permettre  de revenir dans la fenêtre des comptes 
	 * si jamais on ne veut plus ajouter de nouvel utilisateur, tout en demandant une confirmation
	 * @param event : évènement de l'utilisateur
	 */
	@FXML
	private void handleButtonCancel(ActionEvent event) {
		Alert alert = new Alert(
				AlertType.CONFIRMATION,
				"Are you sure you want to cancel ?",
				ButtonType.YES,
				ButtonType.NO
		);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.YES) {
				
			this.loadSubScene("../compteCourant/CompteCourantList.fxml");
			
		}
		
	}
	/**
	 * Affiche les erreurs relatives ï¿½ la base de donnï¿½es (e.g : champs inexistants, incompatibles, etc...)
	 * @param e : PersistenceException
	 */
	private void processPersistenceException(PersistenceException e) {
		new Alert(AlertType.ERROR, "Database error : "+e.getLocalizedMessage(), ButtonType.OK).showAndWait();
	}
	 
}
