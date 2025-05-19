
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAirline;
import acme.constraints.ValidPhoneNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAirline
@Table(indexes = {
	@Index(columnList = "id"), @Index(columnList = "iataCode")
})
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				name;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{3}$")
	private String				iataCode;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				website;

	@Mandatory
	@Automapped
	@Valid
	private AirlineType			type;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				foundationMoment;

	@Optional
	@Automapped
	@ValidEmail
	private String				email;

	@Optional
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

}
