/*
 *  Copyright (c) 2022 Mercedes-Benz Tech Innovation GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Mercedes-Benz Tech Innovation GmbH - Initial API and Implementation
 *
 */

package org.eclipse.edc.vault.hashicorp;

import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.runtime.metamodel.annotation.Provides;
import org.eclipse.edc.spi.http.EdcHttpClient;
import org.eclipse.edc.spi.security.CertificateResolver;
import org.eclipse.edc.spi.security.Vault;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.types.TypeManager;

@Provides({ CertificateResolver.class })
@Extension(value = HashicorpVaultExtension.NAME)
public class HashicorpVaultExtension implements ServiceExtension {


    public static final String NAME = "Hashicorp Vault";


    @Inject
    private EdcHttpClient httpClient;

    @Inject
    private TypeManager typeManager;

    private Vault vault;

    @Override
    public String name() {
        return NAME;
    }

    @Provider
    public Vault vault() {
        return vault;
    }

    @Provider
    public Vault hashicorpVault(ServiceExtensionContext context) {
        if (vault == null) {
            var config = HashicorpVaultClientConfig.create(context);
            var client = new HashicorpVaultClient(config, httpClient, typeManager.getMapper());

            vault = new HashicorpVault(client, context.getMonitor());
        }
        return vault;
    }

    @Provider
    public CertificateResolver vaultResolver(ServiceExtensionContext context) {
        return new HashicorpCertificateResolver(hashicorpVault(context), context.getMonitor().withPrefix("HashicorpVaultCertificateResolver"));
    }

}