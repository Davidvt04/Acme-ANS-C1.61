
package acme.entities.assistanceAgent;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class assistanceAgent extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@Automapped
	@ValidString(min = 3, max = 3, pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(max = 255)
	private String				spokenLanguages;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	private Moment				moment;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				briefBio;

	@Optional
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidUrl
	private String				photo;

}
