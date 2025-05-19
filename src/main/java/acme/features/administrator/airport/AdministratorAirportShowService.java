
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
public class AdministratorAirportShowService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		try {
			if (!super.getRequest().getMethod().equals("GET"))
				super.getResponse().setAuthorised(false);
			else {
				Integer id = super.getRequest().getData("id", Integer.class);
				if (id == null)
					super.getResponse().setAuthorised(false);
				else {
					Airport airport = this.repository.getAirportById(id);
					super.getResponse().setAuthorised(airport != null);
				}
			}

		} catch (Throwable t) {
			super.getResponse().setAuthorised(false);
		}

	}

	@Override
	public void load() {
		Airport airport;
		Integer id = super.getRequest().getData("id", Integer.class);

		airport = this.repository.getAirportById(id);
		super.getBuffer().addData(airport);
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
