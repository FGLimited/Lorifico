package Server.Networking.SQL;

import Logging.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.sql.*;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public class DBContext implements Database {

    private final String connectionString;

    private volatile Connection dbLink;

    public DBContext(final String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public void connect() throws SQLException {
        if(dbLink != null)
            return;

        dbLink = DriverManager.getConnection(connectionString);
    }

    @Override
    public void disconnect() throws SQLException {
        dbLink.close();
        dbLink = null;
    }

    @Override
    public @Nullable <T> List<T> submit(final String queryString, Class<T> type) throws SQLException {

        if(dbLink == null)
            connect();

        // Create new query statement to interrogate the database
        try(Statement request = dbLink.createStatement()) {

            // Interrogate database and get result set
            final ResultSet result = request.executeQuery(queryString);

            // Check if return data is required
            if(type == null)
                return null;

            // Read data and return in requested type objects
            return DBHelper.getResultList(result, type);

        } catch (SQLException se) {
            Logger.log(Logger.LogLevel.Warning, "Error while performing following query: \"" + queryString + "\".\n" + se.getMessage());

            return null;
        }
    }

    @Override
    public void submit(final String queryString) throws SQLException {
        submit(queryString, null);
    }

    @Override
    public @Nullable <T> List<T> call(final String storedProcedureName, Object[] inputParams, Class<T> objectType, Object[] outputParams) throws SQLException {

        if(dbLink == null)
            connect();

        // Build procedure call statement according to total parameters number
        String procedureName = "{call " + storedProcedureName;
        int totalParams = (inputParams != null ? inputParams.length : 0) + (outputParams != null ? outputParams.length : 0);

        // Build procedure call like "{call procName(?, ?, ?)}" adding all input/output parameters as needed
        if(totalParams != 0) {
            procedureName += "(?";

            for (int i = 1; i < totalParams; i++) {
                procedureName += ", ?";
            }

            procedureName += ")}";
        }
        else
            procedureName += "()}";

        // Initialize procedure call statement
        try (CallableStatement procedure = dbLink.prepareCall(procedureName)) {

            // Add all input parameters if any
            if(inputParams != null)
                for (int i = 0; i < inputParams.length; i++)
                    procedure.setObject(i + 1, inputParams[i]);

            // Register all output parameters types if any
            if(outputParams != null)
                for (int i = 0; i < outputParams.length; i++)
                    procedure.registerOutParameter(totalParams - outputParams.length + i + 1, getSQLType(outputParams[i]));

            // Call stored procedure on the database
            procedure.execute();

            // Read output values after the call and store them back in the outputParams array
            if(outputParams != null)
                for (int i = 0; i < outputParams.length; i++)
                    outputParams[i] = procedure.getObject(totalParams - outputParams.length + i + 1);

            // Check if result set is expected
            if(objectType == null)
                return null;

            // Get procedure result set and return data in requested object's type
            return DBHelper.getResultList(procedure.getResultSet(), objectType);
        }
    }

    @Override
    public @Nullable <T> List<T> call(final String storedProcedureName, Object[] inputParams, Class<T> objectType) throws SQLException {
        return call(storedProcedureName, inputParams, objectType, null);
    }

    @Override
    public @Nullable <T> List<T> call(final String storedProcedureName, Class<T> objectType, Object[] outputParams) throws SQLException {
        return call(storedProcedureName, null, objectType, outputParams);
    }

    @Override
    public @Nullable <T> List<T> call(final String storedProcedureName, Class<T> objectType) throws SQLException {
        return call(storedProcedureName, null, objectType, null);
    }

    @Override
    public void call(final String storedProcedureName, Object[] inputParams, Object[] outputParams) throws SQLException {
        call(storedProcedureName, inputParams, null, outputParams);
    }

    @Override
    public void call(final String storedProcedureName, Object[] params, boolean inputOnly) throws SQLException {
        if(inputOnly)
            call(storedProcedureName, params, null, null);
        else
            call(storedProcedureName, null, null, params);
    }

    /**
     * Return SQL object type of given object
     *
     * @param obj Java object
     * @return SQL object type number
     */
    @Contract(pure = true)
    private int getSQLType(Object obj) {

        if(obj instanceof Boolean)
            return Types.BIT;

        if(obj instanceof Character)
            return Types.CHAR;

        if(obj instanceof Byte)
            return Types.TINYINT;

        if(obj instanceof Short)
            return Types.SMALLINT;

        if(obj instanceof Integer)
            return Types.INTEGER;

        if(obj instanceof Long)
            return Types.BIGINT;

        if(obj instanceof Float)
            return Types.FLOAT;

        if(obj instanceof Double)
            return Types.DOUBLE;

        if(obj instanceof String)
            return Types.VARCHAR;

        if(obj instanceof java.util.Date)
            return Types.DATE;

        return Types.JAVA_OBJECT;
    }
}
