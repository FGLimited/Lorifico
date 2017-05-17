package Server.Networking.SQL;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fiore on 10/05/2017.
 */
class DBHelper {

    /**
     * Try to store a result set in a list of objects of a given type
     *
     * @param resultSet Result set still open
     * @param objectType Type of object to bind
     * @param <T> Object type
     * @return List of objects of the given type initialized as in the result set when possible
     * @throws SQLException Thrown when there are problems reading from the result set
     */
    @Contract("null, _ -> null")
    static @Nullable <T> List<T> getResultList(ResultSet resultSet, Class<T> objectType) throws SQLException {

        // Check result set consistency
        if(resultSet == null)
            return null;

        // Retrieve usable fields from given type
        Field[] fields = getAvailableFields(resultSet.getMetaData(), objectType);

        final ArrayList<T> resultList = new ArrayList<>();

        try {
            // Get parameterless constructor of given type
            final Constructor<T> constructorT = objectType.getDeclaredConstructor();
            constructorT.setAccessible(true);

            // Populate list until result set ends
            while (resultSet.next()) {

                T current = constructorT.newInstance();

                // Populate each field with corresponding data from the result set
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i] == null)
                        continue;

                    fields[i].set(current, resultSet.getObject(i + 1, fields[i].getType()));
                }

                // Add initialized instance to the result list
                resultList.add(current);
            }

            // Return populated list
            return resultList;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {

            throw new SQLException("Error during instantiation of " + objectType.getName());
        }
    }

    /** Try to bind column names from a result set to field names of a given class
     *
     * @param rsMeta Metadata of the result set to bind to the class
     * @param type Class type to bind
     * @return Array with as many fields as selected columns; if a column could't be bound to a class field
     *         the corresponding position is set null
     * @throws SQLException Throw if there are problems reading result set metadata
     */
    private static Field[] getAvailableFields(ResultSetMetaData rsMeta, Class type) throws SQLException {
        final Field[] fields = new Field[rsMeta.getColumnCount()];

        // Find correct binding between field name in given class and column name in result set
        for (int i = 0; i < fields.length; i++) {

            try {
                fields[i] = type.getDeclaredField(rsMeta.getColumnName(i + 1));
                fields[i].setAccessible(true);
            } catch (NoSuchFieldException nse) {
                fields[i] = null;
            }
        }

        return fields;
    }
}
