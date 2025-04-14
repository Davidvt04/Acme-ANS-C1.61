
package acme.components;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.service.Service;

@Repository
public interface ServiceRepository extends AbstractRepository {

	@Query("select count(s) from Service s")
	int countServices();

	@Query("select s from Service s")
	List<Service> findAllServices(PageRequest pageRequest);

	default Service findRandomService() {
		Service result;
		int count, index;
		PageRequest page;
		List<Service> list;

		count = this.countServices();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);

			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			list = this.findAllServices(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}

}
