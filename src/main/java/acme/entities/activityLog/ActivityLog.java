
package acme.entities.activityLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidActivityLog;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.flightAssignament.FlightAssignament;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidActivityLog
@Table(indexes = {
    @Index(columnList = "draftMode"),
    @Index(columnList = "flight_assignament_id"), 
    @Index(columnList = "registrationMoment"),
    @Index(columnList = "typeOfIncident"), 
    @Index(columnList = "severityLevel") 
})

public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignament	flightAssignament;

	@Mandatory
	@ValidMoment(past = true) //Sacar el past = true para probarlo
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				typeOfIncident;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2)
	@Automapped
	private Integer				severityLevel;
}
