
package acme.entities.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

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
public class Customer extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				identifier;

	@Mandatory
	@Column(unique = true)
	@Automapped
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				address;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				city;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				country;

	@Optional
	@Automapped
	@ValidNumber(max = 500000)
	private Integer				earnedPoints;

}
