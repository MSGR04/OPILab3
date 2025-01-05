package org.web.lab3.utils;

import org.web.lab3.models.Point;
import org.web.lab3.models.Result;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseManager {
    private static final String dbUrl = ConfigLoader.get("db.url");
    private static final String dbUsername = ConfigLoader.get("db.username");
    private static final String dbPassword = ConfigLoader.get("db.password");
    private static final String dbDriver = ConfigLoader.get("db.driver");
    private static final String CREATE_TABLE_QUERY = """
        CREATE TABLE IF NOT EXISTS results (
            id SERIAL PRIMARY KEY,
            x DOUBLE PRECISION NOT NULL,
            y DOUBLE PRECISION NOT NULL,
            r DOUBLE PRECISION NOT NULL,
            is_hit BOOLEAN NOT NULL,
            timestamp BIGINT NOT NULL
        );
    """;

    static {
        try {
            Class.forName(dbDriver);
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found: " + dbDriver, e);
        }
    }

    private static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during database initialization: " + e.getMessage(), e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    public static void saveResult(Result result) {
        String query = "INSERT INTO results (x, y, r, is_hit, timestamp) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, result.getPoint().getX());
            statement.setDouble(2, result.getPoint().getY());
            statement.setDouble(3, result.getPoint().getR());
            statement.setBoolean(4, result.getIsHit());
            statement.setLong(5, result.getTimestamp());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving the result: " + e.getMessage(), e);
        }
    }

    public static List<Result> fetchResults() {
        String query = "SELECT x, y, r, is_hit, timestamp FROM results";
        List<Result> results = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double r = resultSet.getDouble("r");
                boolean isHit = resultSet.getBoolean("is_hit");
                long timestamp = resultSet.getLong("timestamp");

                Point point = new Point(x, y, r);
                Result result = new Result(point, isHit, timestamp);
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while fetching results: " + e.getMessage(), e);
        }
        Collections.reverse(results);
        return results;
    }
}
