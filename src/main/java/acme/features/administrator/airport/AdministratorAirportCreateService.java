
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.OperationalScope;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport = new Airport();

		super.getBuffer().addData(airport);

	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "city", "country", "operationalScope", "website", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

	}

	@Override
	public void perform(final Airport airport) {

		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		SelectChoices operationalScopes = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "city", "country", "operationalScope", "website", "email", "phoneNumber");
		dataset.put("operationalScopes", operationalScopes);
		super.getResponse().addData(dataset);
	}

}
