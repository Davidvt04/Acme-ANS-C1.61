
package acme.entities.trackingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@OneToOne
	private Claim				claim;

	@Mandatory
	@Automapped
	@ValidMoment
	private Date				lastUpdateMoment;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				step;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 100)
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	private boolean				indicator;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				resolution;

}
