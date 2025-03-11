
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------
	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private Double				claimsResolvedRatio;
	private Double				claimsRejectedRatio;
	private List<String>		topThreeMonthsByClaims;
	private Double				averageLogsPerClaim;
	private Integer				minimumLogsPerClaim;
	private Integer				maximumLogsPerClaim;
	private Double				standardDeviationLogsPerClaim;
	private Double				averageClaimsLastMonth;
	private Integer				minimumClaimsLastMonth;
	private Integer				maximumClaimsLastMonth;
	private Double				standardDeviationClaimsLastMonth;

}
