/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.validation.process.bbb.xcv.sub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificatePolicy;
import eu.europa.esig.dss.diagnostic.jaxb.XmlOID;
import eu.europa.esig.dss.enumerations.CertificatePolicy;
import eu.europa.esig.dss.enumerations.QCStatement;
import eu.europa.esig.dss.policy.jaxb.Level;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks.CertificateQualifiedCheck;

public class CertificateQualifiedCheckTest extends AbstractTestCheck {

	@Test
	public void certificateQualifiedCheckWithQCStatement() throws Exception {
		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlCertificate xc = new XmlCertificate();

		List<XmlOID> qcStatementIds = new ArrayList<>();
		XmlOID oid = new XmlOID();
		oid.setValue(QCStatement.QC_COMPLIANCE.getOid());
		qcStatementIds.add(oid);
		xc.setQCStatementIds(qcStatementIds);

		XmlSubXCV result = new XmlSubXCV();
		CertificateQualifiedCheck cqc = new CertificateQualifiedCheck(i18nProvider, result, new CertificateWrapper(xc), constraint);
		cqc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
	}

	@Test
	public void certificateQualifiedCheckWithCertificate() throws Exception {
		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlCertificate xc = new XmlCertificate();
		List<XmlCertificatePolicy> certPolicies = new ArrayList<>();
		XmlCertificatePolicy oid = new XmlCertificatePolicy();
		oid.setValue(CertificatePolicy.QCP_PUBLIC.getOid());
		certPolicies.add(oid);
		xc.setCertificatePolicies(certPolicies);

		XmlSubXCV result = new XmlSubXCV();
		CertificateQualifiedCheck cqc = new CertificateQualifiedCheck(i18nProvider, result, new CertificateWrapper(xc), constraint);
		cqc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
	}

	@Test
	public void failedCertificateQualifiedCheck() throws Exception {
		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlCertificate xc = new XmlCertificate();

		XmlSubXCV result = new XmlSubXCV();
		CertificateQualifiedCheck cqc = new CertificateQualifiedCheck(i18nProvider, result, new CertificateWrapper(xc), constraint);
		cqc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
	}

}
