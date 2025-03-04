
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				model;

	@Mandatory
	@Automapped
	@Column(unique = true)
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	private Integer				capacity;

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000, max = 50000)
	private Integer				cargoWeight;

	@Mandatory
	@Automapped
	@Valid
	private AircraftStatus		status;

	@Optional
	@Automapped
	private String				details;

}
