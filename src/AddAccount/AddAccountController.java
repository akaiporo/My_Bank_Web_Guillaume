package AddAccount;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import application.ControllerBase;
import application.MainWindowController;
import application.Mediator;
import application.Tools;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import metier.Account;
import metier.AccountType;
import metier.Advisor;
import metier.Agency;
import metier.Assign;
import metier.AssignPK;
import metier.CountryCode;


public class AddAccountController extends ControllerBase {
	private EntityManager em;
	protected Account currentAccount=new Account();
	private Agency newAgency=new Agency();
	private Advisor newAdvisor=new Advisor();
	List<Advisor> advisors = new ArrayList<Advisor>();
	
	@FXML private TextField first_total;
	@FXML private TextField overdraft;
	@FXML private TextField account_number;
	@FXML private TextField interest_rate;
	@FXML private ChoiceBox <Agency> choiceAgency;
	@FXML private ChoiceBox <Advisor> choiceAdvisor;
	@FXML private ChoiceBox <AccountType> choiceAccountType;
	@FXML private ChoiceBox <CountryCode> choiceCountryCode;
	@FXML private DatePicker date_creation;
	@FXML private Button OK;
	@FXML private Button cancel;
	@FXML private AnchorPane accountPane;
	@FXML private Label account_error;

	@Override
	/**
	 * Initialise les ChoiceBox agency,advisor,accounttype,countrycodes
	 */
	public void initialize(Mediator mediator){
		account_error.setText("");
		try {	
			em = mediator.createEntityManager();
			
			List<Agency> agencies = em.createNamedQuery("Agency.findAll", Agency.class).getResultList();
			newAgency.setAgencyName("(new agency)");
			agencies.add(newAgency); //permettra d'ajouter une nouvelle agence
			this.choiceAgency.setItems(FXCollections.observableList(agencies));
			
			newAdvisor.setName("(new");
			newAdvisor.setFirstName("advisor)");
			advisors.add(newAdvisor);
			this.choiceAdvisor.setItems(FXCollections.observableList(advisors));
		
			List<AccountType> accounttypes = em.createNamedQuery("AccountType.findAll", AccountType.class).getResultList();
			this.choiceAccountType.setItems(FXCollections.observableList(accounttypes));
			
			List<CountryCode> countrycodes = em.createNamedQuery("CountryCode.findAll", CountryCode.class).getResultList();
			this.choiceCountryCode.setItems(FXCollections.observableList(countrycodes));
		}
		catch(PersistenceException e) {
			this.processPersistenceException(e);
		}
	}
	
	/**
	 * Gere une action cons√©cutive √† la s√©lection d'une agence ou de (new agency)
	 * @param event : ÈvËnement de l'utilisateur
	 */
	@FXML
	private void addAgency (ActionEvent event){
		ChoiceBox catAgency = (ChoiceBox)event.getTarget();
		Agency tmp=(Agency)catAgency.getValue();
		if (tmp.getAgencyName().equals("(new agency)")){
			/*
			 * Si c'est (new agency) la sous scene correspondante (AddAgencyView) est charg√©e
			 */
			this.loadSubScene("../AddAgency/AddAgencyView.fxml");
		}
		else{
			/*
			 * Si c'est une agence existante on restreint la liste des advisors dans la ChoiceBox advisor
			 */
			choiceAdvisor.getItems().removeAll(advisors);
			Query u = em.createQuery("SELECT a FROM Advisor a WHERE a.agency = :agency", Advisor.class);
			u.setParameter("agency", choiceAgency.getValue());
			advisors = u.getResultList();
			advisors.add(newAdvisor);
			this.choiceAdvisor.setItems(FXCollections.observableList(advisors));
		}
	}
	
	/**
	 * Gere une action cons√©cutive √† la s√©lection de (new advisor)
	 * @param event : ÈvËnement de l'utilisateur
	 */
	@FXML
	private void addAdvisor (ActionEvent event){
		ChoiceBox catAdvisor = (ChoiceBox)event.getTarget();
		Advisor tmp=(Advisor)catAdvisor.getValue();
		/*
		 * Si c'est (new advisor) la sous scene correspondante (AddAdvisor) est charg√©e
		 */
		if (tmp.getName().equals("(new") &&
			tmp.getFirstName().equals("advisor)")){
			this.loadSubScene("../AddAdvisor/AddAdvisorView.fxml");
		}
	}
	
	/**
	 * Gere une action cons√©cutive √† l'utilisation du bouton OK
	 * @param event : ÈvËnement de l'utilisateur
	 */
	@FXML
	private void handleButtonOK (ActionEvent event){
		/*
		 * L'objet <Account> ("currentAccount",cr√©√© vide) est rempli via les setters en testant chaque champ 
		 */
		try{
			currentAccount.setAgency(choiceAgency.getValue());
		}
		catch (NullPointerException e){
			account_error.setText("Please choose an existing agency or create one");
		}
		try{
			currentAccount.setAccountNumber(this.account_number.getText());
		}
		catch (IllegalArgumentException e){
			account_error.setText(e.getMessage());
		}
		try{
			currentAccount.setCreationDate(Tools.LocalDateToDate(this.date_creation.getValue()));
		}
		catch (IllegalArgumentException e){
			account_error.setText(e.getMessage());
		}
		try{
			currentAccount.setFirstTotal(Double.parseDouble(this.first_total.getText()));
		}
		catch (IllegalArgumentException e){
			account_error.setText(e.getMessage());
		}
		try{
			currentAccount.setOverdraft(Integer.parseInt(this.overdraft.getText()));
		}
		catch(IllegalArgumentException e){
			account_error.setText(e.getMessage());
		}
		try{
			currentAccount.setInterestRate(Double.parseDouble(this.interest_rate.getText()));
		}
		catch(IllegalArgumentException e){
			account_error.setText(e.getMessage());
		}
		try{
			currentAccount.setCountryCode(choiceCountryCode.getValue());
		}
		catch (NullPointerException e){
			account_error.setText("Please choose an existing country code");
		}
		try{
			currentAccount.setAccountType(choiceAccountType.getValue());
		}
		catch (NullPointerException e){
			account_error.setText("Please choose an existing account type");
		}
		
		/*
		 * Ajout dans la base de l'objet account
		 */
		em.getTransaction().begin();
		em.persist(currentAccount);
		try{
			em.getTransaction().commit();
			this.linkAccount(currentAccount);
		}
		catch(Exception e){
			em.getTransaction().rollback();
			return;
		}
		/*
		 * La page principale de l'application est charg√©e
		 */
		this.loadSubScene("../compteCourant/CompteCourantList.fxml");
	}
	
	/**
	 * Lie le compte nouvelle crÈÈ ‡ l'utilisateur actuel
	 * @param currentAccount : compte qui vient d'Ítre crÈÈ
	 */
	private void linkAccount(Account currentAccount){
		AssignPK tmp = new AssignPK(currentAccount.getId(), MainWindowController.currentOwner.getId());
		Assign assign = new Assign(tmp);
		em.getTransaction().begin();
		em.persist(assign);
		try{
			em.getTransaction().commit();
		}
		catch(Exception e){
			em.getTransaction().rollback();
			return;
		}
	}
	
	/**
	 * Gere une action cons√©cutive √† l'utilisation du bouton cancel : chargement de la page principale
	 * @param event : ÈvËnement de l'utilisateur
	 */
	@FXML
	private void handleButtonCancel (ActionEvent event){
		this.loadSubScene("../compteCourant/CompteCourantList.fxml");
	}
	
	/**
	 * Affiche les erreurs relatives √† la base de donn√©es (e.g : champs inexistants, incompatibles, etc...)
	 * @param e : PersistenceException
	 */
	private void processPersistenceException(PersistenceException e) {
		new Alert(AlertType.ERROR, "Database error : "+e.getLocalizedMessage(), ButtonType.OK).showAndWait();
	}
	
	
}
