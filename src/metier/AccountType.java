package metier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="accounttype")
@NamedQuery(name="AccountType.findAll", query="SELECT at FROM AccountType at")
public class AccountType {

	/* VARIABLES */
	private int id;
	private String accountType;
	
	/* CONSTRUCTOR */
	public AccountType(String account_type){
		if(account_type.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.accountType = account_type;
	}
	public AccountType(){
	}
	
	/* GETTERS & SETTERS */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId(){
		return this.id;
	}
	
	@Column(name="account_type")
	public String getAccountType(){
		return this.accountType;
	}
	public void setAccountType(String acc){
		if(acc.isEmpty()){
			throw new IllegalArgumentException();
		}
		this.accountType = acc;
	}
	public void setId(int val){
		if(val <= 0){
			throw new IllegalArgumentException();
		}
		this.id = val;
	}
	
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof AccountType){
			AccountType tmp = (AccountType)obj;
		
			if(tmp.getAccountType().equals(this.getAccountType())){
				return true;
			}
			else return false;
		}
		else return false;	
	}
	
	@Override
	/**
	 * Return : le nom du type de compte
	 */
	public String toString() {
		return this.accountType;
	}
}
