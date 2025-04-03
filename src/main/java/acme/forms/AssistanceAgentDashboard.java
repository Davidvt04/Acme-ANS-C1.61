
package acme.forms;

import java.util.Collection;

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
	private Collection<String>	topThreeMonthsByClaims;
	private Double				averageLogsPerClaim;
	private Long				minimumLogsPerClaim;
	private Long				maximumLogsPerClaim;
	private Double				standardDeviationLogsPerClaim;
	private Double				averageClaimsLastMonth;
	private Integer				minimumClaimsLastMonth;
	private Integer				maximumClaimsLastMonth;
	private Double				standardDeviationClaimsLastMonth;

}
