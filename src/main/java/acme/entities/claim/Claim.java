
package acme.entities.claim;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.ClaimStatus;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				registrationMoment;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				passengerEmail;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				description;

	@Mandatory
	@Automapped
	@Valid
	private ClaimType			type;


	@Transient
	public ClaimStatus getStatus() {
		TrackingLogRepository repository = SpringHelper.getBean(TrackingLogRepository.class);
		TrackingLog tracking = repository.findOrderTrackingLog(this.getId()).get(0);
		return tracking.getStatus();
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne
	private Leg				leg;

}
