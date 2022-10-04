/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.elasticsearch.client.model.ResultList;
import org.eclipse.kapua.service.storable.model.AbstractStorableListResult;

/**
 * {@link MessageListResult} implementation.
 *
 * @since 1.0.0
 */
public class MessageListResultImpl extends AbstractStorableListResult<DatastoreMessage> implements MessageListResult {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public MessageListResultImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param resultList The {@link ResultList} to add.
     * @since 1.0.0
     */
    public MessageListResultImpl(ResultList<DatastoreMessage> resultList) {
        super(resultList.getResult(), resultList.getTotalCount());
    }

}
