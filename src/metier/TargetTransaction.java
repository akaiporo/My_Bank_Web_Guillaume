package metier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="targettransaction")
@NamedQueries({
	@NamedQuery(name="TargetTransaction.findAll", query="SELECT a FROM TargetTransaction a"),
	@NamedQuery(name="TargetTransaction.findAllName", query="SELECT a.targetName FROM TargetTransaction a")
})
public class TargetTransaction {

	public TargetTransaction(){
	}
	public TargetTransaction(String target_name,String IBAN) {
		
		if (target_name.isEmpty()){
			throw new IllegalArgumentException("wording cannot be empty");
		}
		if (IBAN.length() != 27){
			throw new IllegalArgumentException("IBAN is composed of 27 car in France");
		}
		if (!IBAN.toUpperCase().startsWith("FR")){
			throw new IllegalArgumentException("IBAN begins with 'FR' in France");
		}
		
		this.target_name=target_name;
		this.IBAN=IBAN;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() {
		return this.id;
	}
	public void setId(int val){
		if(val <= 0){
			throw new IllegalArgumentException();
		}
		this.id = val;
	}
	@Column(name="target_name")
	public String getTargetName(){
		return this.target_name;
	}
	private void setTargetName(String name){
		this.target_name = name;
	}
	@Column(name="IBAN")
	public String getIBAN(){
		return this.IBAN;
	}
	private void setIBAN(String iban){
		this.IBAN = iban;
	}
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof TargetTransaction){
			TargetTransaction tmp = (TargetTransaction)obj;
			
			if(tmp.getTargetName().equals(this.getTargetName()) && 
			   tmp.getIBAN().equals(this.getIBAN())){
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	@Override
	/**
	 * Return le nom de la cible
	 */
	public String toString(){
		return this.target_name;
	}
	
	private int id;
	private String target_name;
	private String IBAN;

}
