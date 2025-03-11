
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidLongText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString
	private String				spokenLanguages;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				moment;

	@Optional
	@Automapped
	@ValidLongText
	private String				briefBio;

	@Optional
	@Automapped
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidUrl
	private String				photo;

	/*
	 * 
	 * @Mandatory
	 * @Valid
	 * @ManyToOne
	 * private Airline airline;
	 * 
	 * 
	 */

}
