
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 8, max = 9, pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidString(min = 6, max = 15, pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				specialisation;

	@Mandatory
	// @Valid by default
	@Automapped
	private boolean				passedAnnualHealthTest;

	@Mandatory
	@ValidNumber(min = 0, max = 80)
	@Automapped
	private Integer				experienceYears;

	@Optional
	@ValidLongText
	@Automapped
	private String				certifications;
}
