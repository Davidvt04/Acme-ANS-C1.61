
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidCustomer;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCustomer
//@Table(indexes = {
//@Index(columnList = "user_account_id")
//})
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidLongText
	private String				address;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				city;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				country;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 500000)
	private Integer				earnedPoints;

}
