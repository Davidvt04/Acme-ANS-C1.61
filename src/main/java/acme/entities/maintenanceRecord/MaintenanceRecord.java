
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidLongText;
import acme.constraints.ValidNextInspection;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					moment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenaceRecordStatus	status;

	@Mandatory
	@ValidNextInspection
	@Temporal(TemporalType.TIMESTAMP)
	private Date					nextInspectionDueTime;

	@Mandatory
	@ValidMoney(min = 0, max = 999999999)
	@Automapped
	private Money					estimatedCost;

	@Optional
	@ValidLongText
	@Automapped
	private String					notes;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician				technician;
}
