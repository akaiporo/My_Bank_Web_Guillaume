package metier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="periodunit")
@NamedQuery(name="PeriodUnit.findAll", query="SELECT a FROM PeriodUnit a")

public class PeriodUnit {
	
	public PeriodUnit(){
	}

	public PeriodUnit(String unit) {
		
		if (unit.isEmpty()){
			throw new IllegalArgumentException("unit cannot be empty, eg 'day' or 'month'");
		}
		
		this.unit=unit;
	}
	
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
	
	@Column(name="unit")
	public String getUnit(){
		return this.unit;
	}
	private void setUnit(String unit){
		this.unit = unit;
	}
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof PeriodUnit){
			PeriodUnit tmp = (PeriodUnit)obj;
		
			if(tmp.getUnit().equals(this.getUnit())){
				return true;
			}
			else return false;
		}
		else return false;	
	}
	
	@Override
	/**
	 * Return l'unité de périodicité (mensuelle, hebdomadaire, trimestrielle...)
	 */
	public String toString(){
		return this.unit;
	}
	
	private int id;
	private String unit;
}
