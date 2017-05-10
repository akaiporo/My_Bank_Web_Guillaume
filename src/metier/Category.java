package metier; 

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Category")
@NamedQueries({
	@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c"),
	@NamedQuery(name="Category.findAllName", query="SELECT c.wording FROM Category c")
}) 


public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	public Category(String wording,Category category) {
	
		if (wording.isEmpty()){
			throw new IllegalArgumentException("wording cannot be empty");
		}
		this.wording=wording;
		this.category=category; 
	}
	public Category() {
		
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getId() { 
		return this.id;
	}
	public String getWording(){
		return this.wording;
	}
	public void setWording(String wording){
		if (wording.isEmpty()){
			throw new IllegalArgumentException("wording cannot be empty");
		}
		this.wording = wording;
	}
	@ManyToOne
	@JoinColumn(name="id_subCategory")
	public Category getCategory(){
		return this.category;
	}
	public void setCategory(Category cat){
		this.category = cat;
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
		if(obj instanceof Category){
			Category tmp = (Category)obj;
			if(tmp.getWording() == null){
				tmp.setWording("");
			}
			if(tmp.getWording().equals(this.getWording())){
				if(tmp.getCategory() != null){
					return tmp.getCategory().equals(this.getCategory());
				}
				else {
					return (tmp.getCategory() == null && this.getCategory() == null);
				}
			}
			else return false;
		}
		else return false;
	}
	
	@Override
	/**
	 * Return le nom de la catégorie
	 */
	public String toString(){
		return this.wording;
	}
	
	private int id;
	private String wording;
	private Category category;
}
