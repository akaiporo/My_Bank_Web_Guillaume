package metier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import application.Tools;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Person {
	/**
	 * 
	 * @param name : name of the person
	 * @param firstname : first name of the person
	 * @param phonenumber : phone number of the person
	 * @param mail : email of the person
	 */
	
	public Person(String name, String firstname, String phonenumber, String mail) {
		if(name.isEmpty()) {
			throw new IllegalArgumentException("The name cannot be empty");
		}
		if(firstname.isEmpty()) {
			throw new IllegalArgumentException("The firstname cannot be empty");
		}
		if(Tools.eraseChar(phonenumber,"\\s").length()<4||Tools.eraseChar(phonenumber,"\\s").length()>11) {
			throw new IllegalArgumentException("phonenumber must contain between 4 and 11 numbers");
		}
		if(!Tools.checkMail(mail)){
			throw new IllegalArgumentException("mail must be of a valid format eg toto@titi.tutu");
		}
				
		this.name = name;
		this.firstname = firstname;
		this.phonenumber = phonenumber;
		this.mail = mail;

	}
	
	public Person(){
		
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
	
	public String getName() {
		return this.name;
	}
	
	public String getFirstName() {
		return this.firstname;
	}
	
	public String getPhoneNumber() {
		return this.phonenumber;
	}
	public String getMail() {
		return this.mail;
	}
	
	public void setName (String nam) {
		this.name=nam;
	}
	public void setFirstName (String nam) {
		this.firstname=nam;
	}
	public void setPhoneNumber (String phone) {
		this.phonenumber=phone;
	}
	public void setMail (String mail) {
		this.mail=mail;
	}
	
	
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof Person){
			Person tmp = (Person)obj;
		
			if(((tmp.getName()==null || this.getName()==null) || tmp.getName().equals(this.getName()))
				&& 
				((tmp.getFirstName()==null || this.getFirstName()==null) || tmp.getFirstName().equals(this.getFirstName())) 
				&&	 
				((tmp.getPhoneNumber()==null||this.getPhoneNumber()==null) || tmp.getPhoneNumber().equals(this.getPhoneNumber()))
				&&
				((tmp.getMail()==null || this.getMail()==null) || tmp.getMail().equals(this.getMail())))
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	private int id;
	private String name;
	private String firstname;
	private String phonenumber;
	private String mail;
}
