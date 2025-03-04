
package acme.entities.trackingLog;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrakingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@OneToOne
	private Claim				claim;

	@Mandatory
	@Automapped
	@ValidMoment
	private Moment				lastUpdateMoment;

	@Mandatory
	@Automapped
	@ValidString(max = 50)
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
	@ValidString(max = 255)
	private String				resolution;

}
