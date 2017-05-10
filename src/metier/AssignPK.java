package metier;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the assign database table.
 * 
 */
@Embeddable
public class AssignPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_account")
	private int idAccount;

	@Column(name="id_owner")
	private int idOwner;

	public AssignPK() {
	}
	public AssignPK(int idAcc, int idOwn){
		this.idAccount = idAcc;
		this.idOwner = idOwn;
	}
	public int getIdAccount() {
		return this.idAccount;
	}
	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}
	public int getIdOwner() {
		return this.idOwner;
	}
	public void setIdOwner(int idOwner) {
		this.idOwner = idOwner;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AssignPK)) {
			return false;
		}
		AssignPK castOther = (AssignPK)other;
		return 
			(this.idAccount == castOther.idAccount)
			&& (this.idOwner == castOther.idOwner);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idAccount;
		hash = hash * prime + this.idOwner;
		
		return hash;
	}
}