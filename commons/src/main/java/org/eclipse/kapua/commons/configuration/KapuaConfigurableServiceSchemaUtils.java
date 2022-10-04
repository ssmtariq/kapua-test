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
package org.eclipse.kapua.commons.configuration;

import java.io.File;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.CommonsEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.SimpleSqlScriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable service database schema utilities.
 *
 * @since 1.0
 */
public class KapuaConfigurableServiceSchemaUtils {

    private static final Logger logger = LoggerFactory.getLogger(KapuaConfigurableServiceSchemaUtils.class);

    private KapuaConfigurableServiceSchemaUtils() {
    }

    public static final String DEFAULT_PATH = "src/main/sql/H2";
    public static final String DEFAULT_FILTER = "sys_*.sql";
    public static final String DROP_FILTER = "sys_*_drop.sql";

    /**
     * Executes the database scripts in the specified path matching the specified filter
     *
     * @param path
     * @param fileFilter
     *            file names pattern
     */
    public static void scriptSession(String path, String fileFilter) {
        EntityManager em = null;
        try {

            logger.info("Running database scripts...");

            em = CommonsEntityManagerFactory.getEntityManager();
            em.beginTransaction();

            SimpleSqlScriptExecutor sqlScriptExecutor = new SimpleSqlScriptExecutor();
            sqlScriptExecutor.scanScripts(path, fileFilter);
            sqlScriptExecutor.executeUpdate(em);

            em.commit();

            logger.info("...database scripts done!");
        } catch (KapuaException e) {
            logger.error("Database scripts failed: {}", e.getMessage());
            if (em != null) {
                em.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }

    /**
     * Executes the create schema files contained in the path
     *
     * @param path
     * @throws KapuaException
     */
    public static void createSchemaObjects(String path)
            throws KapuaException {
        String pathSep = String.valueOf(File.separatorChar);
        String sep = path.endsWith(pathSep) ? "" : pathSep;
        scriptSession(path + sep + DEFAULT_PATH, DEFAULT_FILTER);
    }

    /**
     * Executes the drop schema files contained in the path
     *
     * @param path
     */
    public static void dropSchemaObjects(String path) {
        String pathSep = String.valueOf(File.separatorChar);
        String sep = path.endsWith(pathSep) ? "" : pathSep;
        scriptSession(path + sep + DEFAULT_PATH, DROP_FILTER);
    }
}
