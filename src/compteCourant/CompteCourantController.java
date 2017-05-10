package compteCourant;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import application.ControllerBase;
import application.Main;
import application.MainWindowController;
import application.Mediator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import javafx.scene.control.Alert.AlertType;
import metier.Account;
import metier.Assign;
import metier.AssignPK;
import metier.Category;
import metier.PeriodUnit;
import metier.PeriodicTransaction;
import metier.TargetTransaction;
import metier.TransactionType;
import ribcalculation.RIBCalculationController;

public class CompteCourantController extends ControllerBase {

	@FXML private AnchorPane anchorPane;
	@FXML private TableView<PeriodicTransaction> listTransactions;
	@FXML private CheckBox chkCycle;
	@FXML private TextField txtLabel;
	@FXML private TextField txtDescription;
	@FXML private TextField txtValeur;
	@FXML private TextField txtCycle;
	@FXML private DatePicker dateCreated;
	@FXML private DatePicker dateCycle;
	@FXML private ChoiceBox<Category> choiceCategory;
	@FXML private ChoiceBox<TargetTransaction> choiceTarget;
	@FXML private ChoiceBox<TransactionType> choiceType;
	@FXML private ChoiceBox<Account> choiceAccount;
	@FXML private ChoiceBox<PeriodUnit> choiceCycle;
	@FXML private Button btnApply;
	@FXML private Button btnEdit;
	@FXML private Button btnDelete;  
	@FXML private Label errDate;
	@FXML private Label errLibele;
	@FXML private Label errType;
	@FXML private Label errTarget;
	@FXML private Label errCategory;
	@FXML private Label errValue;
	@FXML private Label labelCycleValue;
	@FXML private Label labelCycleEnd;
	@FXML private Label labelCycleType;
	@FXML private Label thresh;
	@FXML private PieChart pieChart;
	@FXML private TextField threshAlert;
	@FXML private Label resultThrashUpdate;

	
	//non FXML var
		  private EntityManager em;
		  private List<PeriodicTransaction> Transactions;
		  private List<Category> categories;
		  private List<TargetTransaction> targets;
		  private List<TransactionType> transactionType;
		  private List<Account> accounts;
		  private List<PeriodUnit> periodUnits;
		  private List<PieChart.Data> transactionsValues;
		  private Account currentAccount;
		  private PeriodicTransaction currentTransaction;
		  final Label caption = new Label("");
		  
	@Override
	/**
	 * Initialize la plupart des listes (ChoiceBoxes et TableView)
	 */
	public void initialize(Mediator mediator) {
		em = mediator.createEntityManager();
		Main.getPrimaryStage().setMinWidth(1020);
		Main.getPrimaryStage().setMinHeight(600);
		
		/**
		 * Ajouter le pieChart a stackPane
		 */
		caption.setTextFill(Color.DARKORANGE);
		caption.setStyle("-fx-font: 24 arial;");
		anchorPane.getChildren().add(caption);
		
		List<Assign> assignList = em.createNamedQuery("Assign.findAll").getResultList();
		this.accounts = new ArrayList<Account>();
		for(Assign a : assignList)
		{
			if(a.getId().getIdOwner() == MainWindowController.currentOwner.getId())
			{
				this.accounts.add(em.find(Account.class, a.getId().getIdAccount()));
			}
		}
	
		this.choiceAccount.setItems(FXCollections.observableList(accounts));
		this.choiceAccount.getSelectionModel().selectFirst();
		
		Account tmp = this.choiceAccount.getSelectionModel().getSelectedItem();
		if(tmp != null){
			Account account = em.find(Account.class, tmp.getId());
			this.setTransactionList(account);
		}
		
	
		this.categories = em.createNamedQuery("Category.findAll").getResultList();
		this.choiceCategory.setItems(FXCollections.observableList(categories));
		
		this.targets = em.createNamedQuery("TargetTransaction.findAll").getResultList();
		this.choiceTarget.setItems(FXCollections.observableList(targets));
		
		this.transactionType = em.createNamedQuery("TransactionType.findAll").getResultList();
		this.choiceType.setItems(FXCollections.observableList(transactionType));
		
		this.periodUnits = em.createNamedQuery("PeriodUnit.findAll").getResultList();
		this.choiceCycle.setItems(FXCollections.observableList(periodUnits));
		
		this.chkCycle.setSelected(false);
		this.initCycleOptionsVisibility(false);
		if(transactionsValues != null){
			this.pieChart.setData(FXCollections.observableList(transactionsValues));
		}
		
		this.listTransactions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PeriodicTransaction>() {
			@Override 
			//Pour une raison inconnue, le "changed" ne se trigger pas si le Libelé et la valeur sont les mêmes 
			//(quand bien même les autres champs sont différents)
			public void changed(ObservableValue<? extends PeriodicTransaction> arg0, PeriodicTransaction oldVal, PeriodicTransaction newVal) {
				updateForm(newVal); 
			}
		});	
		this.threshAlert.setOnKeyPressed(new EventHandler<KeyEvent>()
	    {
	        @Override
	        public void handle(KeyEvent ke)
	        {
	        	TextField val = (TextField)ke.getTarget();
	            if (ke.getCode().equals(KeyCode.ENTER) && !val.equals(threshAlert.getText()))
	            {
	                updateAlert(Integer.parseInt(val.getText()));
	            }
	        }
	    });
	}
	
	/**
	 * Crée la liste des transactions lors d'un changement de compte
	 * @param account :  compte sélectionner dans la choiceBox
	 */
	private void setTransactionList(Account account){
		//Si c'est la première fois qu'on lance l'apply, la liste est vide
		//On récupère donc la liste de transaction générées avec la création du compte passer en paramètre
		if(Transactions == null){
			this.Transactions = account.getTransactions();

		}
		//Si non, on clear la liste et on la recrée avec le nouveau compte. 
		else{
			this.listTransactions.getItems().removeAll(Transactions);
			Query q =  em.createQuery("SELECT pt FROM PeriodicTransaction pt WHERE pt.account = :account");
			q.setParameter("account",account);
			this.Transactions = q.getResultList();
		}
		this.listTransactions.setItems(FXCollections.observableList(Transactions));
		
		this.calculTotal();
	
		this.setPieChart();
	}
	/**
	 * Met à jour le seuil d'alerte en BDD
	 * @param value : nouvelle valeur
	 */
	private void updateAlert(int value){
		Account tmp = em.find(Account.class, currentAccount.getId());
		em.getTransaction().begin();
		try {
			tmp.setTransactions(Transactions);
			tmp.setAlertThresh(value);
			em.getTransaction().commit();
			this.resultThrashUpdate.setText("OK !");
			this.resultThrashUpdate.setTextFill(Paint.valueOf("GREEN"));
			this.resultThrashUpdate.setVisible(true);
			
			new java.util.Timer().schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	resultThrashUpdate.setVisible(false);
			            }
			        }, 
			        5000 
			);
		}
		catch(RollbackException e) {
			this.resultThrashUpdate.setText("Erreur !");
			this.resultThrashUpdate.setTextFill(Paint.valueOf("RED"));
			this.resultThrashUpdate.setVisible(true);
		}
		
		
	}
	
	/**
	 * Calcul le solde en fonction du solde de départ et des lignes de compte
	 */
	private void calculTotal(){
		double solde = this.currentAccount.getFirstTotal();
		for(PeriodicTransaction pt : Transactions){
			solde += pt.getTransactionValue();
		}
		if(solde <= this.currentAccount.getAlertThresh()){
			Alert alert  = new Alert(AlertType.CONFIRMATION, "Vous venez de dépasser votre seuil d'alerte !", ButtonType.OK);
			String text = String.format("Votre seuil d'alerte est de %01d. \\nVous êtes actuellement à %.2f", this.currentAccount.getAlertThresh(), solde);
			alert.setContentText(text);
			alert.showAndWait();
		}
		this.thresh.setText(String.format("%s", Double.toString(solde)));
		if(solde < (currentAccount.getOverdraft()*-1)){
			this.thresh.setTextFill(Paint.valueOf("RED"));
		}
	}
	
	/**
	 * Remplie la PieChart avec les valeurs des transactions du compte sélectionné
	 */
	private void setPieChart(){
		
		//Arrive pas à le trier, "duplicate children" quoique que je fasse dès que je veux donner le choix
		//Entre update une data ou ajouter une nouvelle ligne.
		this.transactionsValues = new ArrayList<PieChart.Data>();
		this.transactionsValues.add(new PieChart.Data("void", 0));
		for(PeriodicTransaction t : this.Transactions){
			PieChart.Data tmp = new PieChart.Data(t.getCategory().getWording(), t.getTransactionValue());
			//Retourne une erreur (deux threads essaient d'accéder à la même ressource), mais l'idée est la
			/*for(PieChart.Data data : transactionsValues){
				if(tmp.getName().equals(data.getName())){
					transactionsValues.get(i).setPieValue(transactionsValues.get(i).getPieValue()+tmp.getPieValue());
				}
				else{
					this.transactionsValues.add(tmp);
				}
			}*/
			this.transactionsValues.add(tmp);
			this.pieChart.setVisible(false);
			this.pieChart.setVisible(true);
		}
		
		this.transactionsValues.remove(0);
		int total = 0; 
		for (final PieChart.Data data : pieChart.getData()){
			total+= data.getPieValue();
		}
		//Obligé, le "handle" nécessite des variables de type "final"
		final int finalTotal = total;
		for (final PieChart.Data data : pieChart.getData()) {
		    data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
		        new EventHandler<MouseEvent>() {
		            @Override public void handle(MouseEvent e) {
		            	//mieux placer le label
		                caption.setLayoutX(e.getX());
		                caption.setLayoutY(e.getY());
		                caption.setText(String.format("%s : %.2f %%", data.getName(), (data.getPieValue()*100)/finalTotal));
		             }
		        });
		}
	}
	
	@FXML
	/**
	 * Récupère le compte sélectionné dans la liste, puis set crée la listes de transactions
	 * @param event : event trigger par l'utilisateur
	 */
	public void handleAccount(ActionEvent event){
		em = this.getMediator().createEntityManager();
		ChoiceBox choiceAccount = (ChoiceBox)event.getTarget();
		this.currentAccount = (Account)choiceAccount.getValue();
		this.currentAccount = em.find(Account.class, currentAccount.getId());
		this.threshAlert.setText(Integer.toString(this.currentAccount.getAlertThresh()));
		this.setTransactionList(currentAccount);
	} 
	/**
	 * Récupère les données de la ligne sélectionnée, et remplie le formulaire avec
	 * @param newTransaction transaction actuellement sélectionnée par l'utilisateur
	 * @return true si la récupération des données s'est bien passées
	 */
	private boolean updateForm(PeriodicTransaction newTransaction) {
		
		this.btnEdit.setDisable(false);
		this.btnDelete.setDisable(false);
		
		this.currentTransaction = newTransaction;
		try{
			this.txtLabel.setText(this.currentTransaction.getWording());
			this.txtDescription.setText(this.currentTransaction.getDescription());
			this.txtValeur.setText(Double.toString(this.currentTransaction.getTransactionValue()));
			this.dateCreated.setValue(this.currentTransaction.getDateOperation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			this.choiceCategory.setValue(this.currentTransaction.getCategory());
			this.choiceType.setValue(this.currentTransaction.getTransactionType());
			this.choiceTarget.setValue(this.currentTransaction.getTargetTransaction());
			if(this.currentTransaction.getPeriodUnit() != null || this.currentTransaction.getDayNumber() != 0){
				this.chkCycle.setSelected(true); 
				this.initCycleOptionsVisibility(true);
				this.choiceCycle.setValue(this.currentTransaction.getPeriodUnit());
				this.txtCycle.setText(Integer.toString(this.currentTransaction.getDayNumber()));
				if(this.currentTransaction.getEndDateTransaction() != null){
					this.dateCycle.setValue(this.currentTransaction.getEndDateTransaction().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				}
			}
			else {
				this.chkCycle.setSelected(false);
				this.initCycleOptionsVisibility(false);
				this.choiceCycle.setValue(null);
				this.txtCycle.setText("0");
				this.dateCycle.setValue(null);
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
		

	}
	/**
	 * Sauvegarde les données du formulaire lors d'une édition (fait suite à updateForm)
	 */
	@FXML
	public void saveForm() {
		Alert alert  = new Alert(AlertType.CONFIRMATION, "La tâche est modifiée. Enregistrer les modifications ?", ButtonType.YES, ButtonType.CANCEL);
		
		alert.showAndWait();
		
		ButtonType result = alert.getResult();
		
		if(result == ButtonType.CANCEL) {
			return;			
		}
		else if(result == ButtonType.YES){
			boolean isNew = this.currentTransaction.getId()==0;
			ObservableList<PeriodicTransaction> transactions = this.listTransactions.getItems();
			boolean err=false;
			
			if(this.dateCreated.getValue()==null) {
				this.errDate.setVisible(true);
				err=true;
			}
			if(this.txtLabel.getText().isEmpty()) {
				this.errLibele.setVisible(true);
				err=true;
			}
			if(this.txtValeur.getText().isEmpty()) {
				this.errValue.setVisible(true);
				err=true;
			}
			if(this.choiceType.getValue()==null) {
				this.errType.setVisible(true);
				err=true;
			}
			if(this.choiceTarget.getValue()==null) {
				this.errTarget.setVisible(true);
				err=true;
			}	
			if(this.choiceCategory.getValue()==null) {
				this.errCategory.setVisible(true);
				err=true;
			}	
			if(err) {
				return;
			}
			this.currentTransaction.setDateOperation(Date.from(this.dateCreated.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			Date dateOP = this.currentTransaction.getDateOperation();
			this.currentTransaction.setWording(this.txtLabel.getText());
			String wording = this.currentTransaction.getWording();
			this.currentTransaction.setCategory(this.choiceCategory.getValue());
			Category cat = this.currentTransaction.getCategory();
			this.currentTransaction.setTransactionType(this.choiceType.getValue());
			TransactionType type = this.currentTransaction.getTransactionType();
			this.currentTransaction.setTargetTransaction(this.choiceTarget.getValue());
			TargetTransaction target = this.currentTransaction.getTargetTransaction();
			this.currentTransaction.setTransactionValue(Double.parseDouble(this.txtValeur.getText()));
			double val = this.currentTransaction.getTransactionValue();
	
			
			try {
				EntityManager em = getMediator().createEntityManager();
				PeriodicTransaction periodicTransaction = em.find(PeriodicTransaction.class, this.currentTransaction.getId());
				em.getTransaction().begin();
				try {					
				
					periodicTransaction.setCategory(cat);
					periodicTransaction.setDateOperation(dateOP);
					periodicTransaction.setWording(wording);
					periodicTransaction.setTransactionType(type);
					periodicTransaction.setTargetTransaction(target);
					periodicTransaction.setTransactionValue(val);
				
					
					em.getTransaction().commit();
					this.refreshTransaction();	
				}
				catch(RollbackException e) {
					em.getTransaction().rollback();
					return;
				}
			}
			catch(PersistenceException e) {
				this.processPersistenceException(e);
				return;
			}
			
			double solde = Double.parseDouble(this.thresh.getText());
			this.calculTotal();
			
		}
	}
	/**
	 * Affiche ou non les champs du formulaire relatifs à la périodicité d'une transaction
	 * @param visibility : true ou false
	 */
	private void initCycleOptionsVisibility(boolean visibility){
		this.choiceCycle.setVisible(visibility);
		this.txtCycle.setVisible(visibility);
		this.dateCycle.setVisible(visibility);
		this.labelCycleEnd.setVisible(visibility);
		this.labelCycleValue.setVisible(visibility);
		this.labelCycleType.setVisible(visibility);
	}
	
	/**
	 * Récupère le chkBox, et initialise la visibilité en fonction
	 * @param event : event trigger par l'utilisateur
	 */
	@FXML
	private void showCycleOptions(ActionEvent event){
			if(this.chkCycle.isSelected()){
				this.initCycleOptionsVisibility(true);
			}
			else{
				this.initCycleOptionsVisibility(false);
			}
	}
	
	/**
	 * Au click sur le bouton, essaie de créer une nouvelle transaction, de l'ajouter à la liste et de le push en BDD
	 * @param event : event trigger par l'utilisateur
	 */
	@FXML
	private void handleBtnNew(ActionEvent event) {
		PeriodicTransaction transaction = new PeriodicTransaction();
		transaction.setAccount(this.currentAccount);
		
		/**
		 * DEBUT Tests et gestions des erreurs
		 */
		try{
			double value = Double.parseDouble(this.txtValeur.getText());
			transaction.setTransactionValue(value);
			this.errValue.setVisible(false);
		}
		catch(Exception e){
			this.errValue.setVisible(true);
			return;
		}
		try{
			transaction.setWording(this.txtLabel.getText());
			this.errLibele.setVisible(false);
		}
		catch(Exception e){
			this.errLibele.setVisible(true);
			return;
		}
		if(this.chkCycle.isSelected()){
			try{
				Date date = (Date.from(this.dateCycle.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				transaction.setEndDateTransaction(date);
				this.errDate.setVisible(false);
			}
			catch(Exception e){
			
				this.errDate.setVisible(true);
				return;
			}
			try{
				int value = Integer.parseInt(this.txtCycle.getText());
				transaction.setDayNumber(value);
				this.errValue.setVisible(false);
			}
			catch(Exception e){
				return;
			}
			transaction.setDescription(this.txtDescription.getText());
			transaction.setPeriodUnit(this.choiceCycle.getValue());
		}
		try{
			Date date = Date.from(this.dateCreated.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			transaction.setDateOperation(date);
			this.errDate.setVisible(false);
		}
		catch(Exception e){
			this.errDate.setVisible(true);
			return;
		}
		try{
			transaction.setCategory(this.choiceCategory.getValue());
			this.errCategory.setVisible(false);
		}
		catch(Exception e){
			this.errCategory.setVisible(true);
			return;
		}
		try{
			transaction.setTransactionType(this.choiceType.getValue());
			this.errType.setVisible(false);
		}
		catch(Exception e){
			this.errType.setVisible(true);
			return;
		}
		try{
			transaction.setTargetTransaction(this.choiceTarget.getValue());
			this.errTarget.setVisible(false);
		}
		catch(Exception e){
			this.errTarget.setVisible(true);
			return;
		}
		try{
			transaction.setAccount(this.choiceAccount.getValue());
			this.errTarget.setVisible(false);
		}
		catch(Exception e){
			return;
		}
		/**
		 * FIN Tests et gestions des erreurs
		 */
		em.persist(transaction);
		
		for(PeriodicTransaction pt : this.Transactions){
			if(pt.equals(transaction)){
				Alert alert  = new Alert(AlertType.CONFIRMATION, "La tâche existe déjà. Voulez-vous tout de même l'ajouter ?", ButtonType.YES, ButtonType.NO);
				alert.showAndWait();
				ButtonType result = alert.getResult();
				if(result == ButtonType.NO) {
					return;			
				}
			}
		}
		em.getTransaction().begin();
		try{
			em.getTransaction().commit();
			this.refreshTransaction(transaction);
		}
		catch(Exception e){
			
			em.getTransaction().rollback();
		}
		this.refreshTransaction();
	}
	
	/**
	 * Supprime une ligne de la base de données
	 */
	@FXML 
	public void deleteForm(){
		PeriodicTransaction periodicTransaction = new PeriodicTransaction();
		try{
			em.getTransaction().begin();
			periodicTransaction = em.find(PeriodicTransaction.class, this.currentTransaction.getId());
			em.remove(periodicTransaction);
			em.getTransaction().commit();
		}
		catch(Exception e){
			return;
		}
		
		
		this.removeTransaction(periodicTransaction);
		this.calculTotal();
	}
	/**
	 * rafraichi la liste des transactions dans le cas d'un ajout de transaction
	 * @param transaction : transaction ajoutée précédemment
	 */
	private void refreshTransaction(PeriodicTransaction transaction){
		this.Transactions.add(transaction);
		this.listTransactions.setItems(FXCollections.observableList(Transactions));
		this.calculTotal();
	}
	/**
	 * Rafraichi la vue dans le cas d'une suppression/édition
	 */
	private void refreshTransaction(){
		this.listTransactions.getColumns().get(0).setVisible(false);
		this.listTransactions.getColumns().get(0).setVisible(true);
		this.calculTotal();
	}
	/**
	 * Supprime une transaction de la liste
	 * @param transaction : transaction à supprimer
	 */
	private void removeTransaction(PeriodicTransaction transaction){
		int index = 0;
		for(PeriodicTransaction pt : Transactions){
			if(pt.getId() == transaction.getId()){
				this.listTransactions.getItems().remove(index);
				return;
			}
			else index++;
				
		}
	}
	/**
	 * Affiche les erreurs relatives à la base de données (e.g : champs inexistants, incompatibles, etc...)
	 * @param e : PersistenceException
	 */
	private void processPersistenceException(PersistenceException e) {
		new Alert(AlertType.ERROR, "Database error : "+e.getLocalizedMessage(), ButtonType.OK).showAndWait();
	}

	/**
	 * Envoie sur la page de calcul du rib, et envoie le compte actuel dans le nouveau controlleur
	 */
	@FXML
	private void handleRIB(){
		this.loadSubScene("../ribcalculation/RIBCalculation.fxml");
		Node n = null;
		n = MainWindowController.contentPane.getChildren().get(0);
		RIBCalculationController RIBController = (RIBCalculationController)n.getProperties().get("controllerData");
		System.out.print(this.currentAccount.getAgency());
		RIBController.setAccount(this.currentAccount);
	}
}
