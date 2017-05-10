package metier;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.mindrot.jbcrypt.BCrypt;
import application.Tools;

@Entity
@Table(name="Owner")
@NamedQuery(name="Owner.findAll", query = "SELECT o FROM Owner o")

public class Owner extends Person{
	
	/**
	 * Constructor
	 * @param name : The owner name
	 * @param firstname : 
	 * @param phonenumber : 
	 * @param mail : 
	 * @param birthdate : 
	 * @param login : 
	 * @param pwd : 
	 * @param address : 
	 */
	public Owner(String name, String firstname, String phonenumber, String mail, Date birthdate, String login, String pwd, Address address) {
		super(name, firstname, phonenumber, mail);
		
		if (birthdate == null){
			throw new NullPointerException ("birthdate cannot be null");
		}
		if (birthdate.getTime()>Tools.today().getTime()){
			throw new IllegalArgumentException ("birthdate in the future");
		}
		if (login.isEmpty()){
			throw new IllegalArgumentException ("login cannot be empty");
		}
		if (pwd == null){
			pwd = "";
		}
		if (address == null){
			throw new NullPointerException ("address cannot be null");
		}
		this.birthdate=birthdate;
		this.login=login;
		this.pwd=pwd;
		this.address=address;
			
	}
	public Owner() {
		super();
	}
	@Column(name="birthdate")
	@Temporal(TemporalType.DATE)
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date date){
		if (date == null){
			throw new NullPointerException ("birthdate cannot be null");
		}
		if (date.getTime()>Tools.today().getTime()){
			throw new IllegalArgumentException ("birthdate in the future");
		}
		this.birthdate = date;
	}
	@Column(name="login")
	public String getLogin() {
		return this.login;
	}
	public void setLogin(String login){
		if (login.isEmpty()){
			throw new IllegalArgumentException ("login cannot be empty");
		}
		this.login = login;
	}
	@Column(name="pwd")
	public String getPwd() {
		return this.pwd;
	}
	public void setPwd(String pwd){
		if (pwd == null){
			pwd = "";
		}
		this.pwd = pwd;
	}
	@ManyToOne
	@JoinColumn(name="id_address")
	public Address getAddress() {
		return this.address;
	}
	public void setAddress(Address address){
		if (address == null){
			throw new NullPointerException ("address cannot be null");
		}
		this.address = address;
	}
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof Owner){
			Owner tmp = (Owner)obj;
		
			if(((tmp.getName()==null || this.getName()==null) || tmp.getName().equals(this.getName()))
				&& 
				((tmp.getFirstName()==null || this.getFirstName()==null) || tmp.getFirstName().equals(this.getFirstName())) 
				&&	 
				((tmp.getPhoneNumber()==null||this.getPhoneNumber()==null) || tmp.getPhoneNumber().equals(this.getPhoneNumber()))
				&&
				((tmp.getMail()==null || this.getMail()==null) || tmp.getMail().equals(this.getMail()))
				&&
				((tmp.getBirthdate()==null||this.getBirthdate()==null) || tmp.getBirthdate().equals(this.getBirthdate()))
				&&
				((tmp.getLogin()==null||this.getLogin()==null) || tmp.getLogin().equals(this.getLogin()))
				&&
				((tmp.getPwd()==null||this.getPwd()==null) || tmp.getPwd().equals(this.getPwd()))
				&&
				((tmp.getAddress()==null||this.getAddress()==null) || tmp.getAddress().equals(this.getAddress())))
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	@Override
	/**
	 * Return le nom et le prénom
	 */
	public String toString() {
		return String.format("%s %s", getName(), this.getFirstName());
	}
	
	private Date birthdate;
	private String login;
	private String pwd;
	private Address address;
	
	

}
