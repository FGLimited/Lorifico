package Server.Networking.SQL;

import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
public interface Database {

    /**
     * Try to connect to the database
     *
     * @throws SQLException Problems connecting to the database
     */
    void connect() throws SQLException;

    /**
     * Disconnect from the database
     *
     * @throws SQLException Problems disconnecting from the database
     */
    void disconnect() throws SQLException;

    /**
     * Submit a query and try to store results in list of object of requested type.
     * Name and type of column and object field must be the same to ensure correct storage.
     * Object type must have a declared parameterless constructor (maybe private).
     *
     * @param queryString Query to be executed in the database
     * @param type Type of object to store results in
     * @param <T> Type of object
     * @return List containing entities from query result set if binding is possible
     * @throws SQLException If there is some error with the query
     */
    @Nullable
    <T> List<T> submit(final String queryString, Class<T> type) throws SQLException;

    /**
     * Execute requested query without returning the result
     *
     * @param queryString Query to execute
     * @throws SQLException If some errors with given query
     */
    void submit(final String queryString) throws SQLException;

    /**
     * Call specified stored procedure and try to store results in list of objects of requested type
     *
     * @param storedProcedureName Stored procedure name
     * @param inputParams Array of objects to be set as input parameter for the stored procedure
     * @param objectType Type of object to try to bind to result set
     * @param outputParams Array of objects to be set as output parameters (object inside the array are changed after the call)
     * @param <T> Type of object to bind
     * @return List of object initialized as in the result set if present and if can be bound
     * @throws SQLException If any problem occurs during the call
     */
    @Nullable
    <T> List<T> call(final String storedProcedureName, Object[] inputParams, Class<T> objectType, Object[] outputParams) throws SQLException;

    /**
     * Call specified procedure without any input/output parameter
     *
     * @param storedProcedureName Stored procedure name
     * @param objectType Type of object to try to bind to result set
     * @param <T> Type of object to bind
     * @return List of object initialized as in the result set if present and if can be bound
     * @throws SQLException If any problem occurs during the call
     */
    @Nullable
    <T> List<T> call(final String storedProcedureName, Class<T> objectType) throws SQLException;

    /**
     * Call specified procedure with only input parameters
     *
     * @param storedProcedureName Stored procedure name
     * @param inputParams Array of objects to be set as input parameter for the stored procedure
     * @param objectType Type of object to try to bind to result set
     * @param <T> Type of object to bind
     * @return List of object initialized as in the result set if present and if can be bound
     * @throws SQLException If any problem occurs during the call
     */
    @Nullable
    <T> List<T> call(final String storedProcedureName, Object[] inputParams, Class<T> objectType) throws SQLException;

    /**
     * Call specified procedure with only output parameters
     *
     * @param storedProcedureName Stored procedure name
     * @param objectType Type of object to try to bind to result set
     * @param outputParams Array of objects to be set as output parameters (object inside the array are changed after the call)
     * @param <T> Type of object to bind
     * @return List of object initialized as in the result set if present and if can be bound
     * @throws SQLException If any problem occurs during the call
     */
    @Nullable
    <T> List<T> call(final String storedProcedureName, Class<T> objectType, Object[] outputParams) throws SQLException;

    /**
     * Call specified procedure without expecting a result set
     *
     * @param storedProcedureName Stored procedure name
     * @param inputParams Array of objects to be set as input parameter for the stored procedure
     * @param outputParams Array of objects to be set as output parameters (object inside the array are changed after the call)
     * @throws SQLException If any problem occurs during the call
     */
    void call(final String storedProcedureName, Object[] inputParams, Object[] outputParams) throws SQLException;

    /**
     * Call specified stored procedure with only input/output parameters as requested and without expecting a result set
     *
     * @param storedProcedureName Stored procedure name
     * @param params Array of input/output objects (if output object inside the array are changed after the call)
     * @param inputOnly True if params are input, false if they are output
     * @throws SQLException If any problem occurs during the call
     */
    void call(final String storedProcedureName, Object[] params, boolean inputOnly) throws SQLException;
}
