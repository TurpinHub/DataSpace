/*
 *  Copyright (c) 2024 Fraunhofer Institute for Software and Systems Engineering
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Fraunhofer Institute for Software and Systems Engineering - initial API and implementation
 *
 */

package org.eclipse.edc.samples.policy;

import org.eclipse.edc.junit.annotations.EndToEndTest;
import org.eclipse.edc.junit.extensions.EdcRuntimeExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static org.eclipse.edc.samples.common.FileTransferCommon.getFileFromRelativePath;
import static org.eclipse.edc.samples.common.NegotiationCommon.checkContractNegotiationState;
import static org.eclipse.edc.samples.common.NegotiationCommon.negotiateContract;
import static org.eclipse.edc.samples.common.PolicyCommon.createAsset;
import static org.eclipse.edc.samples.common.PolicyCommon.createContractDefinition;
import static org.eclipse.edc.samples.common.PolicyCommon.createPolicy;

@EndToEndTest
class Policy01BasicTerminatedTest {
    
    private static final String CONTRACT_OFFER_FILE_PATH = "policy/policy-01-policy-enforcement/resources/contract-request.json";
    static final String PROVIDER_CONFIG_PROPERTIES_FILE_PATH = "policy/policy-01-policy-enforcement/policy-enforcement-provider/config.properties";
    static final String CONSUMER_CONFIG_PROPERTIES_FILE_PATH = "system-tests/src/test/resources/policy/config-us.properties";
    
    @RegisterExtension
    static EdcRuntimeExtension provider = new EdcRuntimeExtension(":policy:policy-01-policy-enforcement:policy-enforcement-provider",
            "provider", Map.of("edc.fs.config", getFileFromRelativePath(PROVIDER_CONFIG_PROPERTIES_FILE_PATH).getAbsolutePath()));
    
    @RegisterExtension
    static EdcRuntimeExtension consumer = new EdcRuntimeExtension(":policy:policy-01-policy-enforcement:policy-enforcement-consumer",
            "consumer", Map.of("edc.fs.config", getFileFromRelativePath(CONSUMER_CONFIG_PROPERTIES_FILE_PATH).getAbsolutePath()));
    
    @Test
    void runSampleSteps() {
        createAsset();
        createPolicy();
        createContractDefinition();
        var negotiationId = negotiateContract(CONTRACT_OFFER_FILE_PATH, "");
        checkContractNegotiationState(negotiationId, "TERMINATED");
    }
}
