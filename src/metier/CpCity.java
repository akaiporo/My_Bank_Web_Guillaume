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
@Table(name="cpcity")
@NamedQueries({
@NamedQuery(name="cpcity.findAll", query="SELECT p FROM CpCity p"),
@NamedQuery(name="cpcity.findAllpostalcode", query ="SELECT p.postalCode FROM CpCity p")
})
public class CpCity {

	private int id;
	private String postalCode;
	private String city;
	
	public CpCity(String postalcode, String city) {
		if(postalcode.length() != 5){
			throw new IllegalArgumentException("Poste code must must contains 5 characters");
		}
		if(city.length() == 0){
			throw new IllegalArgumentException("City can't be empty");
		}
		this.postalCode = postalcode;
		this.city = city;
	}
	
	public CpCity(){
		
	} 
	
	public void setId(int id){
		if(id <= 0){
			throw new IllegalArgumentException("Id can't be null or negativ");
		}
		this.id = id;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId(){
		return this.id;
	}
	@Column(name="postalcode")
	public String getPostalCode(){
		return this.postalCode;
	}
	public String setPostalCode(String postalcode){
		if(postalcode.length() != 5){
			throw new IllegalArgumentException("Poste code must must contains 5 characters");
		}
		return this.postalCode = postalcode;
	}
	@Column(name="city")
	public String getCity(){
		return this.city;
	}
	public String setCity(String city){
		
		if(city.length() == 0){
			throw new IllegalArgumentException("City can't be empty");
		}
		return this.city = city;
	}
	
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof CpCity){
			CpCity tmp = (CpCity)obj;
	
			if(tmp.getPostalCode().equals(this.getPostalCode()) &&	 
				tmp.getCity().equals(this.getCity())){
				return true;
			}
			else return false;
		}
		else return false;
	}

}
