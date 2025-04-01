
package acme.features.administrator.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claim.Claim;

@GuiController
public class AdministratorClaimController extends AbstractGuiController<Administrator, Claim> {

	@Autowired
	private AdministratorClaimListService	listService;

	@Autowired
	private AdministratorClaimShowService	showService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

	}
}
