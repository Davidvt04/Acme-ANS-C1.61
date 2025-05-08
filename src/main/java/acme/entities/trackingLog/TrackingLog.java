
package acme.entities.trackingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidShortText;
import acme.constraints.ValidTrackingLog;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
//@Table(indexes = {
//@Index(columnList = "claim_id")
//})
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				lastUpdateMoment;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				step;

	@Mandatory
	@Automapped
	@ValidScore
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	@Valid
	private ClaimStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				resolution;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
