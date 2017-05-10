package metier; 

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="CountryCode")
@NamedQuery(name="CountryCode.findAll", query="SELECT c FROM CountryCode c")

public class CountryCode  implements Serializable {
	private static final long serialVersionUID = 1L;
	

	/* VARIABLES */
	private int id;
	private String countryCode;
	
	/* CONSTRUCTORS */
	public CountryCode(String code){

		if(code.length() != 2){
			throw new IllegalArgumentException("Le code pays doit contenir exactement deux chiffres");
		}
		
		this.countryCode = code;
	}
	public CountryCode() {
		
	}
	/* GETTERS & SETTERS */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId(){
		return this.id;
	}
	@Column(name="country_code")
	public String getCountryCode(){
		return this.countryCode;
	}
	
	public void setCountryCode(String code){
		if(code.length() != 2){
			throw new IllegalArgumentException("Le code pays doit contenir exactement deux chiffres");
		}
		this.countryCode = code;
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
		if(obj instanceof CountryCode){
			CountryCode tmp = (CountryCode)obj;
		
			if(tmp.getCountryCode().equals(this.getCountryCode())){
				return true;
			}
			else return false;
		}
		else return false;	
	}
	
	@Override
	/**
	 * Return le code pays
	 */
	public String toString() {
		return this.countryCode;
	}
}
