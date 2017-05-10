package metier;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="periodictransaction")
@NamedQuery(name="PeriodicTransaction.findAll", query="SELECT t FROM PeriodicTransaction t")
public class PeriodicTransaction{

	/* VARIABLES */
	private int id;
	private String wording;
	private Double transaction_value;
	private Date date_operation;
	private Date end_date_transaction;
	private int day_number;
	private String description;
	private TransactionType transactionType;
	private TargetTransaction targetTransaction;
	private Category category;
	private PeriodUnit periodUnit;
	//Utile uniquement pour faire le lien avec la bdd
	private Account account;

	public PeriodicTransaction(){
	}
	
	/**
	 * 
	 * @param wording : libelé
	 * @param transaction_value : valeur
	 * @param date_operation : date active
	 * @param end_date_transaction : date de fin de cycle (optionnelle)
	 * @param day_number : jour actif du cycle
	 * @param description : optionnelle
	 * @param transactionType : type de transaction (virement, chèque...)
	 * @param targetTransaction : compte ciblé
	 * @param category	: 	
	 * @param periodUnit : type de cycle (mensuel,hebdo...)
	 */
	public PeriodicTransaction(String wording, Double transaction_value, Date date_operation, Date end_date_transaction,
							  int day_number, String description, TransactionType transactionType, TargetTransaction targetTransaction, Category category,
							  PeriodUnit periodUnit){
		if(wording.isEmpty()){
			throw new IllegalArgumentException("Le libelï¿½ ne peut ï¿½tre vide");
		}
		if(transaction_value == null){
			throw new NullPointerException("La valeur de la ligne ne peut ï¿½tre vide ou null");
		}
		if(date_operation == null){
			throw new NullPointerException("La date de dï¿½but ne peut ï¿½tre null");
		}
		if(transactionType == null){
			throw new NullPointerException("le type de transaction ne peut ï¿½tre null");
		}
		if(category == null){
			throw new NullPointerException("le type de transaction ne peut ï¿½tre null");
		}
		if((periodUnit == null)){
			if(day_number > 0)
			throw new NullPointerException("Une period ou un jour d'ï¿½chï¿½ance ne peuvent ï¿½tre dï¿½clarï¿½ l'un sans l'autre");
		}
		else if(day_number <= 0){
			if(periodUnit!= null){
				throw new NullPointerException("Une period ou un jour d'ï¿½chï¿½ance ne peuvent ï¿½tre dï¿½clarï¿½ l'un sans l'autre");
			}
		}
		if(targetTransaction == null){
			throw new NullPointerException("la cible ne peut ï¿½tre null");
		}
		if(description == null){
			this.description = "";
		}
		this.wording = wording;
		this.transaction_value = transaction_value;
		this.date_operation = date_operation;
		this.end_date_transaction = end_date_transaction;
		this.day_number = day_number;
		this.description = description;
		this.transactionType = transactionType;
		this.targetTransaction = targetTransaction;
		this.category = category;
		this.periodUnit = periodUnit;
	}
	
	/* GETTERS & SETTERS */
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId(){
		return this.id;
	}
	public void setId(int val){
		if(val <= 0){
			throw new IllegalArgumentException();
		}
		this.id = val;
	}
	
	@Column(name="wording")
	public String getWording(){
		return this.wording;
	}
	public void setWording(String wording){
		this.wording = wording;
	}
	
	@Column(name="transaction_value")
	public double getTransactionValue(){
		
		return this.transaction_value;
	}
	public void setTransactionValue(Double val){
		if(val == null){
			throw new NullPointerException("La valeur de la ligne ne peut ï¿½tre vide ou null");
		}
		this.transaction_value = val;
	}
	
	@Column(name="date_operation")
	@Temporal(TemporalType.DATE)
	public Date getDateOperation(){
		return this.date_operation;
	}
	
	public void setDateOperation(Date date){
		if(date == null){
			throw new NullPointerException("La date ne peut ï¿½tre null");
		}
		this.date_operation = date;
	}
	@Column(name="end_date_transaction")
	@Temporal(TemporalType.DATE)
	public Date getEndDateTransaction(){
		return this.end_date_transaction;
	}
	
	public void setEndDateTransaction(Date date){
		this.end_date_transaction = date;
	}
	@Column(name="day_number")
	public int getDayNumber(){
		return this.day_number;
	}
	public void setDayNumber(int day){
		if(day <= 0){
			throw new IllegalArgumentException("La cycle ne peut être négatif");
		}
		else if(this.getPeriodUnit() == null){
			throw new IllegalAccessError("L'unité n'a pas été déclarée. Utilisez la méthod setPeriodParam()");
		}
		if(day < 0){
			throw new IllegalArgumentException("La cycle ne peut ï¿½tre nï¿½gatif");
		}
		this.day_number = day;
	}
	@Column(name="description")
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String desc){
		this.description = desc;
	}
	@ManyToOne
	@JoinColumn(name="id_transactiontype")
	public TransactionType getTransactionType(){
		return this.transactionType;
	}
	public void setTransactionType(TransactionType tt){
		if(tt == null){
			throw new NullPointerException("Le type ne peut ï¿½tre null");
		}
		this.transactionType = tt;
	}
	@ManyToOne
	@JoinColumn(name="id_targettransaction")
	public TargetTransaction getTargetTransaction(){
		return this.targetTransaction;
	}
	public void setTargetTransaction(TargetTransaction target){
		if(target == null){
			throw new NullPointerException("La cible ne peut ï¿½tre null");
		}
		this.targetTransaction = target;
	}
	@ManyToOne
	@JoinColumn(name="id_category")
	public Category getCategory(){
		return this.category;
	}
	public void setCategory(Category cat){
		if(cat == null){
			throw new NullPointerException("La catï¿½gorie ne peut ï¿½tre null");
		}
		this.category = cat;
	}
	@ManyToOne
	@JoinColumn(name="id_periodunit")
	public PeriodUnit getPeriodUnit(){
		return this.periodUnit;
	}
	public void setPeriodUnit(PeriodUnit pu){
		if(this.day_number == 0){
			throw new IllegalAccessError("Le numéro de jour cyclique n'a pas été déclaré. Utilisez la méthod setPeriodParam()");
		}
		this.periodUnit = pu;
	}
	
	/**
	 * Set les deux paramètres étant donnés qu'ils sont liés
	 * @param pu : type de périodicité (annuelle, mensuelle...)
	 * @param daynumber : Jour actif(3e jour de chaque mois, 6e jour de la semaine...)
	 */
	public void setPeriodParam(PeriodUnit pu, int daynumber){
		if(pu != null && daynumber > 0){
			this.periodUnit = pu;
			this.day_number = daynumber;
		}
		else{
			throw new IllegalArgumentException("Le type de cycle et le jour actif doivent être tout les deux renseigné");
		}
	}
	
	@ManyToOne
	@JoinColumn(name="id_account")
	public Account getAccount(){
		return this.account;
	}
	public void setAccount(Account acc){
		this.account = acc;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PeriodicTransaction){
			PeriodicTransaction tmp = (PeriodicTransaction)obj;
			if(tmp.getDescription() == null){
				tmp.setDescription("");
			}
			
			if(tmp.getEndDateTransaction() == null &&  (tmp.getPeriodUnit() != null &&  tmp.getDayNumber() != 0)){
				if(tmp.getWording().equals(this.getWording()) &&
						   tmp.getDateOperation().equals(this.getDateOperation()) &&
						   tmp.getDayNumber() == this.getDayNumber() &&
						   tmp.getDescription().equals(this.getDescription()) &&
						   tmp.getPeriodUnit().equals(this.getPeriodUnit()) &&
						   tmp.getTargetTransaction().equals(getTargetTransaction()) &&
						   tmp.getTransactionValue() == this.getTransactionValue() &&
						   tmp.getTransactionType().equals(this.getTransactionType()) &&
						   tmp.getCategory().equals(this.getCategory())){
								return true;
				}
				else return false;
			}
			
			else if(tmp.getEndDateTransaction() == null &&  tmp.getPeriodUnit() == null &&  tmp.getDayNumber() == 0){
				if(tmp.getWording().equals(this.getWording()) &&
				   tmp.getDateOperation().equals(this.getDateOperation()) &&
				   tmp.getDescription().equals(this.getDescription()) &&
				   tmp.getTargetTransaction().equals(getTargetTransaction()) &&
			       tmp.getTransactionValue() == this.getTransactionValue() &&
				   tmp.getTransactionType().equals(this.getTransactionType()) &&
				   tmp.getCategory().equals(this.getCategory())){
							return true;
					}
					else return false;
			}
			else{
				if(tmp.getWording().equals(this.getWording()) &&
				   tmp.getDateOperation().equals(this.getDateOperation()) &&
				   tmp.getDayNumber() == this.getDayNumber() &&
				   tmp.getDescription().equals(this.getDescription()) &&
				   tmp.getEndDateTransaction().equals(this.getEndDateTransaction()) &&
				   tmp.getPeriodUnit().equals(this.getPeriodUnit()) &&
				   tmp.getTargetTransaction().equals(getTargetTransaction()) &&
				   tmp.getTransactionValue() == this.getTransactionValue() &&
				   tmp.getTransactionType().equals(this.getTransactionType()) &&
				   tmp.getCategory().equals(this.getCategory())){
						return true;
				}
				else return false;
			}
		}
		else return false;
	}

}