/*
 * Copyright 2007-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.module.schedule.support;

import net.ymate.module.schedule.IScheduler;
import net.ymate.platform.core.persistence.base.Type;
import net.ymate.platform.persistence.jdbc.IDatabase;
import net.ymate.platform.persistence.jdbc.IDatabaseConfig;
import net.ymate.platform.persistence.jdbc.IDatabaseDataSourceAdapter;
import net.ymate.platform.persistence.jdbc.JDBC;
import net.ymate.platform.persistence.jdbc.transaction.Transactions;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerConfigException;
import org.quartz.impl.jdbcjobstore.JobStoreCMT;
import org.quartz.impl.jdbcjobstore.SimpleSemaphore;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author 刘镇 (suninformation@163.com) on 2025/1/2 18:39
 * @since 1.0.2
 */
public class QuartzSchedulerJobStore extends JobStoreCMT {

    public static final String DATA_SOURCE_NAME = "dataSourceName";

    public static final String FULL_DATA_SOURCE_NAME = IScheduler.MODULE_NAME + "." + DATA_SOURCE_NAME;

    public static final String TX_DATA_SOURCE_PREFIX = "txDataSource.";

    public static final String NON_TX_DATA_SOURCE_PREFIX = "nonTxDataSource.";

    @Override
    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        try {
            IDatabase databaseOwner = JDBC.get();
            String dsName = databaseOwner.getOwner().getParam(FULL_DATA_SOURCE_NAME, IDatabaseConfig.DEFAULT_STR);
            IDatabaseDataSourceAdapter dataSourceAdapter = databaseOwner.getDataSourceAdapter(dsName);
            //
            setDataSource(TX_DATA_SOURCE_PREFIX + getInstanceName());
            setDontSetAutoCommitFalse(true);
            //
            DBConnectionManager.getInstance().addConnectionProvider(getDataSource(), new ConnectionProvider() {
                        @Override
                        public Connection getConnection() throws SQLException {
                            try {
                                return databaseOwner.getConnectionHolder(dsName).getConnection();
                            } catch (Exception e) {
                                if (e instanceof SQLException) {
                                    throw (SQLException) e;
                                }
                                throw new SQLException(e);
                            }
                        }

                        @Override
                        public void shutdown() {
                        }

                        @Override
                        public void initialize() {
                        }
                    }
            );
            setNonManagedTXDataSource(NON_TX_DATA_SOURCE_PREFIX + getInstanceName());
            //
            DBConnectionManager.getInstance().addConnectionProvider(getNonManagedTXDataSource(), new ConnectionProvider() {
                        @Override
                        public Connection getConnection() throws SQLException {
                            try {
                                return dataSourceAdapter.getConnection();
                            } catch (Exception e) {
                                if (e instanceof SQLException) {
                                    throw (SQLException) e;
                                } else {
                                    throw new SQLException(e);
                                }
                            }
                        }

                        @Override
                        public void shutdown() {
                        }

                        @Override
                        public void initialize() {
                        }
                    }
            );
            if (StringUtils.equalsIgnoreCase(Type.DATABASE.HSQLDB, dataSourceAdapter.getDialect().getName())) {
                setUseDBLocks(false);
                setLockHandler(new SimpleSemaphore());
            }
            super.initialize(loadHelper, signaler);
        } catch (Exception e) {
            if (e instanceof SchedulerConfigException) {
                throw (SchedulerConfigException) e;
            } else {
                throw new SchedulerConfigException(e.getMessage(), e);
            }
        }
    }

    @Override
    protected void closeConnection(Connection conn) {
        if (Transactions.get() == null) {
            super.closeConnection(conn);
        }
    }
}
