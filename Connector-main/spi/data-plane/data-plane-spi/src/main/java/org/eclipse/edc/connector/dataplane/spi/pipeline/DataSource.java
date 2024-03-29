/*
 *  Copyright (c) 2021 Microsoft Corporation
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

package org.eclipse.edc.connector.dataplane.spi.pipeline;

import java.io.InputStream;
import java.util.stream.Stream;

/**
 * Implements pull semantics for accessing a data source. A data source is composed of one or more named parts.
 * Some implementations may support random access of the underlying part content so that large content transfers can be
 * parallelized.
 */
public interface DataSource extends AutoCloseable {

    /**
     * Opens a stream to the source parts.
     */
    StreamResult<Stream<Part>> openPartStream();

    /**
     * A data source part. This is typically an underlying file or container that the data contains.
     */
    interface Part extends AutoCloseable {

        long SIZE_UNKNOWN = -1;

        /**
         * The part name.
         */
        String name();

        /**
         * The size of the part, or {@link #SIZE_UNKNOWN} if the size cannot be determined.
         */
        default long size() {
            return SIZE_UNKNOWN;
        }

        /**
         * Opens stream to sequentially read the underlying part content.
         */
        InputStream openStream();

        /**
         * Content media type.
         *
         * @return the part media type.
         */
        default String mediaType() {
            return "application/octet-stream";
        }

        @Override
        default void close() throws Exception {
            // no-op
        }
    }

}
