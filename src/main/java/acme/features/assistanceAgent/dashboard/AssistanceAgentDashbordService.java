
package acme.features.assistanceAgent.dashboard;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.ClaimStatus;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashbordService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {
	//Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentDashbordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims = this.repository.findAllClaims();
		Integer userAccountId = this.getRequest().getPrincipal().getAccountId();
		int assistantAgentId = this.getRequest().getPrincipal().getActiveRealm().getId();
		AssistanceAgentDashboard dashboard = new AssistanceAgentDashboard();
		//claimsResolvedRatio
		Long claimsResueltas = claims.stream().filter(x -> x.getStatus() == ClaimStatus.ACCEPTED).count();
		Double claimsResolvedRatio = (double) claimsResueltas / this.repository.findAllClaims().stream().count();
		dashboard.setClaimsResolvedRatio(claimsResolvedRatio);
		//claimsResolvedRatio
		Long claimsRejected = claims.stream().filter(x -> x.getStatus() == ClaimStatus.REJECTED).count();
		Double claimsRejectedRatio = (double) claimsRejected / this.repository.findAllClaims().stream().count();
		dashboard.setClaimsRejectedRatio(claimsRejectedRatio);

		//topThreeMonthsByClaims
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		Map<String, Long> claimsByMonth = claims.stream().filter(claim -> claim.getRegistrationMoment() != null).collect(Collectors.groupingBy(claim -> monthFormat.format(claim.getRegistrationMoment()), Collectors.counting()));

		Collection<String> topThreeMonths = claimsByMonth.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).limit(3).map(Map.Entry::getKey).collect(Collectors.toList());

		dashboard.setTopThreeMonthsByClaims(topThreeMonths);

		//averageLogsPerClaim
		long totalLogs = claims.stream().mapToLong(claim -> this.repository.countLogsByClaimId(claim.getId())).sum();

		double averageLogsPerClaim = claims.size() > 0 ? (double) totalLogs / claims.size() : 0.0;
		dashboard.setAverageLogsPerClaim(averageLogsPerClaim);

		// minimumLogsPerClaim

		Long minLogs = claims.stream().mapToLong(claim -> this.repository.countLogsByClaimId(claim.getId())).min().orElse(0);
		dashboard.setMinimumLogsPerClaim(minLogs);

		// minimumLogsPerClaim
		Long maxLogs = claims.stream().mapToLong(claim -> this.repository.countLogsByClaimId(claim.getId())).max().orElse(0);
		dashboard.setMaximumLogsPerClaim(maxLogs);

		//standardDeviationLogsPerClaim
		List<Long> logsPerClaim = claims.stream().map(claim -> this.repository.countLogsByClaimId(claim.getId())).collect(Collectors.toList());

		double mean = logsPerClaim.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);

		double variance = logsPerClaim.stream().mapToDouble(logs -> Math.pow(logs - mean, 2)).average().orElse(0.0);

		double standardDeviationLogsPerClaim = Math.sqrt(variance);
		dashboard.setStandardDeviationLogsPerClaim(standardDeviationLogsPerClaim);

		// averageClaimsLastMonth
		int thisMonth = MomentHelper.getCurrentMoment().getMonth();
		int pastMonth = thisMonth - 1;
		int year = MomentHelper.getCurrentMoment().getYear();
		if (thisMonth == 1) {
			pastMonth = 12;
			year -= 1;
		}
		Date fecha = MomentHelper.getCurrentMoment();
		fecha.setYear(year);
		fecha.setMonth(pastMonth);
		List<Claim> lastMonthClaims = claims.stream().filter(c -> c.getRegistrationMoment().after(fecha)).toList();
		int totalMonthClaims = lastMonthClaims.size();
		int numAgents = this.repository.findAllAssistanceAgent().size();
		double average = (double) totalMonthClaims / numAgents;
		dashboard.setAverageClaimsLastMonth(average);

		//minimumClaimsLastMonth
		Map<AssistanceAgent, Long> claimsPerAgent = lastMonthClaims.stream().collect(Collectors.groupingBy(Claim::getAssistanceAgent, Collectors.counting()));
		int minimumClaimsLastMonth = claimsPerAgent.isEmpty() ? 0 : Collections.min(claimsPerAgent.values()).intValue();
		dashboard.setMinimumClaimsLastMonth(minimumClaimsLastMonth);

		//maximumClaimsLastMonth
		int maximumClaimsLastMonth = claimsPerAgent.isEmpty() ? 0 : Collections.max(claimsPerAgent.values()).intValue();
		dashboard.setMaximumClaimsLastMonth(maximumClaimsLastMonth);

		//standardDeviationClaimsLastMonth
		double varianceLastMonth = claimsPerAgent.values().stream().mapToDouble(count -> Math.pow(count - average, 2)).sum() / (numAgents - 1);

		double standardDeviationClaimsLastMonth = Math.sqrt(variance);

		dashboard.setStandardDeviationClaimsLastMonth(standardDeviationClaimsLastMonth);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final AssistanceAgentDashboard object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "claimsResolvedRatio", "claimsRejectedRatio", "topThreeMonthsByClaims", "averageLogsPerClaim", "minimumLogsPerClaim", "maximumLogsPerClaim", "standardDeviationLogsPerClaim", "averageClaimsLastMonth",
			"minimumClaimsLastMonth", "maximumClaimsLastMonth", "standardDeviationClaimsLastMonth");

		super.getResponse().addData(dataset);
	}

}
