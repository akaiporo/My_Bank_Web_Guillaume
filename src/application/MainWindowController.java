package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import metier.Account;
import metier.Assign;
import metier.AssignPK;
import metier.Owner;

public class MainWindowController extends ControllerBase{
/*	
	@Inject
	private EntityManagerFactory emf;
*/	
	@FXML
	private StackPane content;
	public static StackPane contentPane;

	
	public static Owner currentOwner;
	
	@Override
	public void initialize(Mediator mediator) {
		try {
			content.getChildren().setAll(loadFxml("../authentification/AuthentificationView.fxml")); // Le mettre dans 'content'
			contentPane = content;
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	public StackPane getStackPane(){
		return this.content;
	}
	/**
	 * Si l'utilisateur clique sur quitter, lui demande confirmation
	 * @param event : évènement de l'utilisateur
	 */
	@FXML
	private void handleMenuFileQuit(ActionEvent event) {
		Alert alert = new Alert(
				AlertType.CONFIRMATION,
				"Vous ï¿½tes sï¿½r de vouloir quitter ?",
				ButtonType.OK,
				ButtonType.CANCEL
		);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}
	/**
	 * Envoie vers la page de création de compte bancaire
	 */
	@FXML
	private void handleButtonAddAccount(){
		if(this.currentOwner == null){
			Alert alert  = new Alert(AlertType.CONFIRMATION, "Vous devez vous connecter avant", ButtonType.CANCEL);
			alert.showAndWait();
		}
		else{
			try {
				content.getChildren().setAll(loadFxml("../AddAccount/AddAccountView.fxml")); // Le mettre dans 'content'
			}
			catch(IOException e) {
				// TODO alert
			}
		}
	}
	/**
	 * Envoie vers la page de création d'utilisateur
	 */
	@FXML
	private void handleButtonAddUser(){
		try {
			content.getChildren().setAll(loadFxml("../AddUser/AddUserView.fxml")); // Le mettre dans 'content'
		}
		catch(IOException e) {
			// TODO alert
		}
	}
	/**
	 * renvoie vers la page de login
	 */
	@FXML
	private void handleButtonDeconnexion(){
		try {


			Alert alert  = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment vous déconnecter ?", ButtonType.YES, ButtonType.NO);
			alert.showAndWait();
			ButtonType result = alert.getResult();
			if(result == ButtonType.NO) {
				return;			
			}
			this.currentOwner = null;
			content.getChildren().setAll(loadFxml("../authentification/AuthentificationView.fxml")); // Le mettre dans 'content'
		}
		catch(IOException e) {
			// TODO alert
		}
	}
	/**
	 * Permet d'éviter la page de login si on a pas de compte (réservé à Michel/Sylvain)
	 */
	@FXML
	private void handleDodgeConnexion(){
		try {
			content.getChildren().setAll(loadFxml("../AddAccount/AddAccountView.fxml"));
		} 
		catch (IOException e) {
		}
	}
	
	@FXML
	private void handleNewLink(){
		/* Peut être utile, à voir
		 * 
		 * if(this.currentOwner == null){
			Alert alert  = new Alert(AlertType.CONFIRMATION, "Vous devez vous connecter avant", ButtonType.CANCEL);
			alert.showAndWait();
		}
		else{
			Alert alert  = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.CANCEL);
			EntityManager em = this.getMediator().createEntityManager();
			//Comptes bancaires
			List<Assign> assignList = em.createNamedQuery("Assign.findAll").getResultList();
			List<Account> accounts = new ArrayList<Account>();
			for(Assign a : assignList)
			{
				if(a.getId().getIdOwner() != MainWindowController.currentOwner.getId())
				{
					accounts.add(em.find(Account.class, a.getId().getIdAccount()));
				}
			}
			ChoiceBox accountList = new ChoiceBox();
			accountList.setItems(FXCollections.observableList(accounts));
			
			//Listes des utilisateurs
			List<Account> owners = em.createNamedQuery("Owner.findAll").getResultList();
			ChoiceBox ownersList = new ChoiceBox();
			ownersList.setItems(FXCollections.observableList(owners));
			
			//Ajout à la fenêtre d'alert
			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(ownersList, 0, 0);
			expContent.add(new Label(" Pourra consulter "), 1, 0);
			expContent.add(accountList, 2, 0);
	
			alert.getDialogPane().setContent(expContent);
			alert.showAndWait();
			
			ButtonType result = alert.getResult();
			
			if(result == ButtonType.CANCEL) {
				return;			
			}
			else if(result == ButtonType.YES){
				Owner owner = (Owner)ownersList.getValue();
				Account account = (Account)accountList.getValue();
				AssignPK tmp = new AssignPK(account.getId(), owner.getId());
				try{
					Assign assign = new Assign(tmp);
					em.persist(assign);
					em.getTransaction().commit();
				}
				catch(Exception e){
					
				}
				
			}
		}*/
	}
}
