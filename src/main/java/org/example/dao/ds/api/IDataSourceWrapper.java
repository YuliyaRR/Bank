package org.example.dao.ds.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataSourceWrapper extends AutoCloseable {

    Connection getConnection() throws SQLException;

}
