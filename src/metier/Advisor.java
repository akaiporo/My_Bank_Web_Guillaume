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

import application.Tools;

@Entity
@Table(name="advisor")
@NamedQuery(name="Advisor.findAll", query="SELECT ad FROM Advisor ad")
public class Advisor extends Person {
	/**
	 * 
	 * @param name : 
	 * @param firstname : 
	 * @param phonenumber : 
	 * @param mail : 
	 * @param date_assignment : 
	 * @param agency : 
	 */

	public Advisor(String name, String firstname, String phonenumber, String mail, Date date_assignment,Agency agency) {
		super(name, firstname, phonenumber, mail);
		
		if(date_assignment == null) {
			throw new NullPointerException("Date assignment cannot be null");
		}
		if(date_assignment.getTime() > Tools.today().getTime()) {  
			throw new IllegalArgumentException ("Date assigment in the future");
		}
		if(agency == null) {
			throw new NullPointerException("Agency cannot be null");
		}
				
	this.date_assignment = date_assignment;
	this.agency = agency;
	
	}
	
	public Advisor(){
		super();
	}
	@Column(name="date_assignment")
	@Temporal(TemporalType.DATE)
	public Date getDateAssignment() {
		return this.date_assignment;
	}
	
	public void setDateAssignment(Date date){
		if(date == null) {
			throw new NullPointerException("Date assignment cannot be null");
		}
		if(date.getTime() > Tools.today().getTime()) {  
			throw new IllegalArgumentException ("Date assigment in the future");
		}
		this.date_assignment = date;
	}
	
	@ManyToOne
	@JoinColumn(name="id_agency")
	public Agency getAgency() {
		return this.agency;
	}
	public void setAgency(Agency agency){
		if(agency == null) {
			throw new NullPointerException("Agency cannot be null");
		}
		this.agency = agency;
	}
	
	@Override
	/**
	 * Return true si tout les champs sont égaux (mais pas les addresses mémoires)
	 */
	public boolean equals(Object obj){
		if(obj instanceof Advisor){
			Advisor tmp = (Advisor)obj;
		
			if(((tmp.getName()==null || this.getName()==null) || tmp.getName().equals(this.getName()))
				&& 
				((tmp.getFirstName()==null || this.getFirstName()==null) || tmp.getFirstName().equals(this.getFirstName())) 
				&&	 
				((tmp.getPhoneNumber()==null||this.getPhoneNumber()==null) || tmp.getPhoneNumber().equals(this.getPhoneNumber()))
				&&
				((tmp.getMail()==null || this.getMail()==null) || tmp.getMail().equals(this.getMail()))
				&&
				((tmp.getDateAssignment()==null||this.getDateAssignment()==null) || tmp.getDateAssignment().equals(this.getDateAssignment()))
				&&
				((tmp.getAgency()==null||this.getAgency()==null) || tmp.getAgency().equals(this.getAgency())))
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	@Override
	/**
	 * Return : Une chaîne formée du nom et du prénom
	 */
	public String toString() {
		return String.format("%s %s", getName(), this.getFirstName());
	}
	
	private Date date_assignment;
	private Agency agency;

}
