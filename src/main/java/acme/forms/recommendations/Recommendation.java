
package acme.forms.recommendations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation {

	private String				name;

	private String				description;

	private String				imageUrl;

	private Double				rating;

	private String				url;

	private RecommendationType	type;

}
