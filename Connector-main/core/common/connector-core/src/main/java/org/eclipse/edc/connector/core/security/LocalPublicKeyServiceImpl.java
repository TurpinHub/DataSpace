/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.connector.core.security;

import org.eclipse.edc.spi.iam.LocalPublicKeyService;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.security.KeyParserRegistry;
import org.eclipse.edc.spi.security.Vault;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link LocalPublicKeyService} which looks-up for the key by id
 * first in the locally cached keys and as fallback in the vault.
 */
public class LocalPublicKeyServiceImpl implements LocalPublicKeyService {
    private final Vault vault;

    private final KeyParserRegistry registry;

    private final Map<String, PublicKey> cachedKeys = new HashMap<>();

    public LocalPublicKeyServiceImpl(Vault vault, KeyParserRegistry registry) {
        this.vault = vault;
        this.registry = registry;
    }

    @Override
    public Result<PublicKey> resolveKey(String id) {
        return resolveFromCache(id)
                .map(Result::success)
                .or(() -> resolveFromVault(id).map(this::parseKey))
                .orElseGet(() -> Result.failure("No public key could be resolved for key-ID '%s'".formatted(id)));
    }

    private Optional<String> resolveFromVault(String id) {
        return Optional.ofNullable(vault.resolveSecret(id));
    }

    private Optional<PublicKey> resolveFromCache(String id) {
        return Optional.ofNullable(cachedKeys.get(id));
    }

    private Result<PublicKey> parseKey(String encodedKey) {
        return registry.parse(encodedKey).compose(pk -> {
            if (pk instanceof PublicKey publicKey) {
                return Result.success(publicKey);
            } else {
                return Result.failure("The specified resource did not contain public key material.");
            }
        });
    }

    public Result<Void> addRawKey(String id, String rawKey) {
        return parseKey(rawKey).onSuccess((pk) -> cachedKeys.put(id, pk)).mapTo();
    }
}
