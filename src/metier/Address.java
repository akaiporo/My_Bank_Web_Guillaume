package metier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="address")
@NamedQuery(name="address.findAll", query="SELECT add FROM Address add")
public class Address {
	/**
	 * 
	 * @param line1 : 
	 * @param line2 : 
	 * @param cpcity : 
	 */

	public Address(String line1, String line2, CpCity cpcity) {
		if(line1.isEmpty()) {
			throw new IllegalArgumentException("The line1 cannot be empty");
		}
		if(line2 == null){
			this.line2 = "";
		}
		else this.line2 = line2;
		if(cpcity == null){
			throw new NullPointerException("PostalCode and City can't be null");
		}
		this.line1 = line1;
		this.cpCity = cpcity;

	}
	public Address(){
		
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
	@Column(name="line1")
	public String getLine1() {
		return this.line1;
	} 
	public void setLine1(String line1){
		if(line1.isEmpty()) {
			throw new IllegalArgumentException("The line1 cannot be empty");
		}
		this.line1 = line1;
	}
	@Column(name="line2")
	public String getLine2() {
		return this.line2;
	}
	public void setLine2(String line2){
		if(line2 == null){
			this.line2 = "";
		}
		else this.line2 = line2;
	}
	@ManyToOne
	@JoinColumn(name="id_postalcode")
	public CpCity getCpCity() {
		return this.cpCity;
	}
	public void setCpCity(CpCity cpc){
		if(cpc == null){
			throw new NullPointerException("PostalCode and City can't be null");
		}
		this.cpCity = cpc;
	}
	
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof Address){
			Address tmp = (Address)obj;
		
			if(tmp.getLine1().equals(this.getLine1()) && 
				tmp.getLine2().equals(this.getLine2()) &&	 
				tmp.getCpCity().equals(this.getCpCity())){
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	private int id;
	private String line1;
	private String line2;
	private CpCity cpCity;
}
		
