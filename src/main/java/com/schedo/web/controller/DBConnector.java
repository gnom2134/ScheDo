package com.schedo.web.controller;

import java.sql.*;

public class DBConnector {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    public DBConnector() throws Exception {
        DriverManager.registerDriver((java.sql.Driver) Class.forName("org.postgresql.Driver").newInstance());
//        Scanner file = new Scanner(new File("db_config.txt"));
        connection = DriverManager.getConnection("url", "user", "password");
//        connection = DriverManager.getConnection(file.nextLine(), file.nextLine(), file.nextLine());
//        file.close();
    }

    public void createPreparedStatement(String sqlStatement) throws SQLException {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        statement = connection.prepareStatement(sqlStatement);
    }

    public ResultSet executePreparedStatementWithResult() throws SQLException {
        resultSet = statement.executeQuery();
        return resultSet;
    }

    public void executePreparedStatement() throws SQLException {
        statement.execute();
    }

    public void setParameter(int index, String parameter) throws SQLException {
        statement.setString(index, parameter);
    }

    public void setParameter(int index, boolean parameter) throws SQLException {
        statement.setBoolean(index, parameter);
    }

    public void setParameter(int index, int parameter) throws SQLException {
        statement.setInt(index, parameter);
    }

    public void endSession() throws SQLException {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
