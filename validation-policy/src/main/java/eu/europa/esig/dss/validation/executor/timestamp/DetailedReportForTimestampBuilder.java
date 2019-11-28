package eu.europa.esig.dss.validation.executor.timestamp;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europa.esig.dss.detailedreport.jaxb.XmlBasicBuildingBlocks;
import eu.europa.esig.dss.detailedreport.jaxb.XmlDetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.Context;
import eu.europa.esig.dss.policy.ValidationPolicy;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.executor.AbstractDetailedReportBuilder;
import eu.europa.esig.dss.validation.process.vpftsp.ValidationProcessForTimeStamps;

public class DetailedReportForTimestampBuilder extends AbstractDetailedReportBuilder {

	public DetailedReportForTimestampBuilder(DiagnosticData diagnosticData, ValidationPolicy policy, Date currentTime) {
		super(diagnosticData, policy, currentTime);
	}

	XmlDetailedReport build() {

		XmlDetailedReport detailedReport = init();

		Map<String, XmlBasicBuildingBlocks> bbbs = executeAllBasicBuildingBlocks();
		detailedReport.getBasicBuildingBlocks().addAll(bbbs.values());
		
		// TODO : long-term validation

		XmlSignature signatureAnalysis = new XmlSignature();
		
		List<TimestampWrapper> timestamps = diagnosticData.getTimestampList();
		executeTimestampsValidation(signatureAnalysis, bbbs, timestamps);
		
		// TODO : timestamp qualification
		
		detailedReport.getSignatures().add(signatureAnalysis);

		return detailedReport;
	}

	private Map<String, XmlBasicBuildingBlocks> executeAllBasicBuildingBlocks() {
		Map<String, XmlBasicBuildingBlocks> bbbs = new LinkedHashMap<String, XmlBasicBuildingBlocks>();
		process(diagnosticData.getAllRevocationData(), Context.REVOCATION, bbbs);
		process(diagnosticData.getTimestampList(), Context.TIMESTAMP, bbbs);
		return bbbs;
	}
	
	private void executeTimestampsValidation(XmlSignature signatureAnalysis, 
			Map<String, XmlBasicBuildingBlocks> bbbs, List<TimestampWrapper> timestamps) {
		if (Utils.isCollectionNotEmpty(timestamps)) {
			for (TimestampWrapper timestamp : timestamps) {
				ValidationProcessForTimeStamps vpftsp = new ValidationProcessForTimeStamps(timestamp, bbbs);
				signatureAnalysis.getValidationProcessTimestamps().add(vpftsp.execute());
			}
		}
	}

}