
package acme.features.administrator.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import acme.client.components.principals.Administrator;
import acme.client.controllers.GuiController;
import acme.client.helpers.Assert;
import acme.client.helpers.PrincipalHelper;

@GuiController
public class AdministratorRecommendationController {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRecommendationCreateService createService;

	// Constructors -----------------------------------------------------------


	@GetMapping("/administrator/recommendation/populate")
	public ModelAndView populateInitial() {
		Assert.state(PrincipalHelper.get().hasRealmOfType(Administrator.class), "acme.default.error.not-authorised");

		ModelAndView result;

		try {
			this.createService.getAllRecommendations();
			PrincipalHelper.handleUpdate();
			result = new ModelAndView();
			result.setViewName("fragments/welcome");
			result.addObject("_globalSuccessMessage", "acme.default.global.message.success");
		} catch (final Throwable oops) {
			result = new ModelAndView();
			result.setViewName("master/panic");
			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			result.addObject("_globalErrorMessage", "acme.default.global.message.error");
			result.addObject("_oops", oops);
		}

		return result;

	}

}
