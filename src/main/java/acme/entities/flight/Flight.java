
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airport.Airport;
import acme.features.authenticated.leg.LegRepository;
import acme.realms.managers.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "flight", indexes = {

	@Index(columnList = "manager_id")

})
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Valid
	@ManyToOne(optional = false)
	@Mandatory
	private Manager				manager;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney(min = 0.00, max = 1000000.00)
	@Automapped
	private Money				cost;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				description;


	@Transient
	public Date getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findFirstScheduledDeparture(this.getId()).orElse(null);
	}

	@Transient
	public Date getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLastScheduledArrival(this.getId()).orElse(null);
	}

	@Transient
	public Integer getNumberOfLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.numberOfLayovers(this.getId());
	}

	@Transient
	public Airport getOriginAirport() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Airport> list = repository.findOrderedOriginAirport(this.getId());
		return list.isEmpty() ? null : list.get(0);
	}

	@Transient
	public Airport getDestinationAirport() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<Airport> list = repository.findOrderedDestinationAirport(this.getId());
		return list.isEmpty() ? null : list.get(0);
	}

	@Transient
	public String getFlightSummary() {
		Airport origin = this.getOriginAirport();
		Airport dest = this.getDestinationAirport();
		String originCity = origin != null ? origin.getCity() : "";
		String destCity = dest != null ? dest.getCity() : "";
		return "Flight: " + originCity + " --> " + destCity;
	}
}
