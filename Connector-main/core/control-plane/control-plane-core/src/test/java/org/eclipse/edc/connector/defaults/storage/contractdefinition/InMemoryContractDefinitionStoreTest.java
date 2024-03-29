/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.edc.connector.defaults.storage.contractdefinition;

import org.eclipse.edc.connector.contract.spi.offer.store.ContractDefinitionStore;
import org.eclipse.edc.connector.contract.spi.testfixtures.offer.store.ContractDefinitionStoreTestBase;
import org.eclipse.edc.connector.core.store.CriterionOperatorRegistryImpl;

class InMemoryContractDefinitionStoreTest extends ContractDefinitionStoreTestBase {

    private final InMemoryContractDefinitionStore store = new InMemoryContractDefinitionStore(CriterionOperatorRegistryImpl.ofDefaults());

    @Override
    protected ContractDefinitionStore getContractDefinitionStore() {
        return store;
    }

}
