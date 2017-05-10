package metier;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the assign database table.
 * 
 */
@Entity
@NamedQuery(name="Assign.findAll", query="SELECT a FROM Assign a")

public class Assign implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AssignPK id;
	
	public Assign() {
	}
	public Assign(AssignPK id){
		this.id = id;
	}
	public AssignPK getId() {
		return this.id;
	}

	public void setId(AssignPK id) {
		this.id = id;
	}

}