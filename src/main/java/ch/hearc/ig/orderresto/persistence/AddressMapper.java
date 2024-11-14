package ch.hearc.ig.orderresto.persistence;

import ch.hearc.ig.orderresto.business.Address;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import java.sql.SQLException;

public class AddressMapper {

    public Address extractAddressFromResultSet(OracleResultSet rs) throws SQLException {
        String postalCode = rs.getString("CODE_POSTAL");
        String locality = rs.getString("LOCALITE");
        String street = rs.getString("RUE");
        String streetNumber = rs.getString("NUM_RUE");
        String country = rs.getString("PAYS");

        return new Address(country, postalCode, locality, street, streetNumber);
    }

    public void populatePreparedStatementWithAddress(Address address, OraclePreparedStatement stmt, int startIndex) throws SQLException {
        stmt.setString(startIndex, address.getPostalCode());
        stmt.setString(startIndex + 1, address.getLocality());
        stmt.setString(startIndex + 2, address.getStreet());
        stmt.setString(startIndex + 3, address.getStreetNumber());
        stmt.setString(startIndex + 4, address.getCountryCode());
    }
}
