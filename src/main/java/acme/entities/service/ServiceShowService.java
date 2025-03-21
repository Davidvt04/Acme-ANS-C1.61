
package acme.entities.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;

public class ServiceShowService extends AbstractGuiService<Any, Service> {

	@Autowired
	private ServiceRepository serviceRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Service service;
		int id = this.getRandomServiceId();
		service = (Service) this.serviceRepository.findById(id).get();
		super.getBuffer().addData(service.getPicture());
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "picture");

		super.getResponse().addData(dataset);
	}

	public int getRandomServiceId() {
		int serviceId = 0;
		int totalServices = (int) this.serviceRepository.count();
		serviceId = (int) (Math.random() * totalServices);
		return serviceId;
	}

}
