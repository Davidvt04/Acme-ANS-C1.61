
package acme.features.authenticated.technician;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.Technician;

@GuiController
public class AuthenticatedTechnicianController extends AbstractGuiController<Authenticated, Technician> {

	@Autowired
	AuthenticatedTechnicianCreateService createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
	}
}
