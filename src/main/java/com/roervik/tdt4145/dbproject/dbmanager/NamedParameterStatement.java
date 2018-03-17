package com.roervik.tdt4145.dbproject.dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class NamedParameterStatement {
    private final PreparedStatement statement;
    private final Map<String, Integer> parameterIndexMap;

    public NamedParameterStatement(String statement, Connection conn) throws SQLException {
        parameterIndexMap = new HashMap<>();
        final StringBuilder preparedStatement = new StringBuilder();
        int i0 = 0, i1 = -1;
        int paramNumber = 1;
        while ((i0 = statement.indexOf(':', i1 + 1)) > 0) {
            preparedStatement.append(statement.subSequence(i1 + 1, i0));
            preparedStatement.append('?');
            i1 = statement.indexOf(':', i0 + 1);
            String parameter = statement.substring(i0 + 1, i1);
            parameterIndexMap.put(parameter, paramNumber++);
        }

        preparedStatement.append(statement.subSequence(i1 + 1, statement.length()));

        this.statement = conn.prepareStatement(preparedStatement.toString());
    }

    public void setInt(final String parameter, final int value) throws SQLException {
        statement.setInt(parameterIndexMap.get(parameter), value);
    }

    public void setString(final String parameter, final String value) throws SQLException {
        statement.setString(parameterIndexMap.get(parameter), value);
    }

    public void setTimestamp(final String parameter, final Timestamp value) throws SQLException {
        statement.setTimestamp(parameterIndexMap.get(parameter), value);
    }

    public void setTimestamp(final String parameter, final LocalDateTime value) throws SQLException {
        Timestamp timestamp = Timestamp.valueOf(value);
        setTimestamp(parameter, timestamp);
    }

    public PreparedStatement getStatement() {
        return statement;
    }
}
