
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.apache.catalina.Manager;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Valid
	@Automapped
	@ManyToOne //Preguntar obligatoriedad manager
	@Mandatory
	private Manager				manager;

	@Mandatory
	@ValidString(min = 50, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	@Valid
	private Indication			indication;

	@Mandatory
	@ValidMoney(min = 0.00, max = 1000000.00)
	@Automapped

	private Money				cost;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				description;


	// Todas estas propiedades derivadas, no se si hay que tomarlas del repo cuando esté, hay que preguntar
	@Transient
	public Date getScheduledDeparture() {
		// TODO: Computar a partir de la primera Leg asociada (si existe)
		return null;
	}

	@Transient
	public Date getScheduledArrival() {
		// TODO: Computar a partir de la última Leg asociada (si existe)
		return null;
	}

	@Transient
	public String getOrigin() {
		// TODO: Obtener a partir de la primera Leg
		return null;
	}

	@Transient
	public String getDestination() {
		// TODO: Obtener a partir de la última Leg
		return null;
	}

	@Transient
	public Integer getNumberOfLayovers() {
		// TODO: Número de paradas intermedias (legs.size() - 1, etc.)
		return null;
	}

}
