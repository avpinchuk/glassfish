/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation.
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.gjc.spi.jdbc40;

import com.sun.enterprise.util.i18n.StringManager;
import com.sun.gjc.spi.ManagedConnectionFactoryImpl;
import com.sun.gjc.spi.base.ResultSetWrapper;

import jakarta.resource.ResourceException;

import java.io.InputStream;
import java.io.Reader;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLXML;
import java.sql.Statement;

import static java.util.logging.Level.SEVERE;

/**
 * Wrapper for ResultSet
 */
public class ResultSetWrapper40 extends ResultSetWrapper {

    protected final static StringManager localStrings = StringManager.getManager(ManagedConnectionFactoryImpl.class);

    /**
     * Creates a new instance of ResultSetWrapper for JDBC 4.0
     *
     * @param statement Statement that is to be wrapped<br>
     * @param resultSet ResultSet that is to be wraped<br>
     * *
     */
    public ResultSetWrapper40(Statement statement, ResultSet resultSet) {
        super(statement, resultSet);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.RowId</code> object in the
     * Java programming language.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @return the column value; if the value is a SQL <code>NULL</code> the value
     * returned is <code>null</code>
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        return resultSet.getRowId(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.sql.RowId</code> object in the
     * Java programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @return the column value ; if the value is a SQL <code>NULL</code> the value
     * returned is <code>null</code>
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        return resultSet.getRowId(columnLabel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        Class<?>[] valueTypes = new Class<?>[] { Integer.TYPE, Class.class };
        try {
            return (T) getMethodExecutor().invokeMethod(resultSet, "getObject", valueTypes, columnIndex, type);
        } catch (ResourceException ex) {
            _logger.log(SEVERE, "jdbc.ex_get_object", ex);
            throw new SQLException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        Class<?>[] valueTypes = new Class<?>[] { String.class, Class.class };
        try {
            return (T) getMethodExecutor().invokeMethod(resultSet, "getObject", valueTypes, columnLabel, type);
        } catch (ResourceException ex) {
            _logger.log(SEVERE, "jdbc.ex_get_object", ex);
            throw new SQLException(ex);
        }
    }

    /**
     * Updates the designated column with a <code>RowId</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead the
     * <code>updateRow</code> or <code>insertRow</code> methods are called to update
     * the database.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param x the column value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        resultSet.updateRowId(columnIndex, x);
    }

    /**
     * Updates the designated column with a <code>RowId</code> value. The updater
     * methods are used to update column values in the current row or the insert
     * row. The updater methods do not update the underlying database; instead the
     * <code>updateRow</code> or <code>insertRow</code> methods are called to update
     * the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param x the column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        resultSet.updateRowId(columnLabel, x);
    }

    /**
     * Retrieves the holdability of this <code>ResultSet</code> object
     *
     * @return either <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     * <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access error occurs or this method is
     * called on a closed result set
     * @since 1.6
     */
    @Override
    public int getHoldability() throws SQLException {
        return resultSet.getHoldability();
    }

    /**
     * Retrieves whether this <code>ResultSet</code> object has been closed. A
     * <code>ResultSet</code> is closed if the method close has been called on it,
     * or if it is automatically closed.
     *
     * @return true if this <code>ResultSet</code> object is closed; false if it is
     * still open
     * @throws SQLException if a database access error occurs
     * @since 1.6
     */
    @Override
    public boolean isClosed() throws SQLException {
        return resultSet.isClosed();
    }

    /**
     * Updates the designated column with a <code>String</code> value. It is
     * intended for use when updating <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns. The updater methods are used to update
     * column values in the current row or the insert row. The updater methods do
     * not update the underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param nString the value for the column to be updated
     * @throws SQLException if the columnIndex is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * the result set concurrency is <code>CONCUR_READ_ONLY</code> or if a database
     * access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        resultSet.updateNString(columnIndex, nString);
    }

    /**
     * Updates the designated column with a <code>String</code> value. It is
     * intended for use when updating <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns. The updater methods are used to update
     * column values in the current row or the insert row. The updater methods do
     * not update the underlying database; instead the <code>updateRow</code> or
     * <code>insertRow</code> methods are called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param nString the value for the column to be updated
     * @throws SQLException if the columnLabel is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * the result set concurrency is <CODE>CONCUR_READ_ONLY</code> or if a database
     * access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        resultSet.updateNString(columnLabel, nString);
    }

    /**
     * Updates the designated column with a <code>java.sql.NClob</code> value. The
     * updater methods are used to update column values in the current row or the
     * insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param nClob the value for the column to be updated
     * @throws SQLException if the columnIndex is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        resultSet.updateNClob(columnIndex, nClob);
    }

    /**
     * Updates the designated column with a <code>java.sql.NClob</code> value. The
     * updater methods are used to update column values in the current row or the
     * insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param nClob the value for the column to be updated
     * @throws SQLException if the columnLabel is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        resultSet.updateNClob(columnLabel, nClob);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>NClob</code> object representing the SQL <code>NCLOB</code>
     * value in the specified column
     * @throws SQLException if the columnIndex is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set or
     * if a database access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        return resultSet.getNClob(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>NClob</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @return a <code>NClob</code> object representing the SQL <code>NCLOB</code>
     * value in the specified column
     * @throws SQLException if the columnLabel is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set or
     * if a database access error occurs
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        return resultSet.getNClob(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the Java
     * programming language.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code> value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return resultSet.getSQLXML(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> as a <code>java.sql.SQLXML</code> object in the Java
     * programming language.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @return a <code>SQLXML</code> object that maps an <code>SQL XML</code> value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return resultSet.getSQLXML(columnLabel);
    }

    /**
     * Updates the designated column with a <code>java.sql.SQLXML</code> value. The
     * updater methods are used to update column values in the current row or the
     * insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param xmlObject the value for the column to be updated
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; this method is called on a closed result set; the
     * <code>java.xml.transform.Result</code>, <code>Writer</code> or
     * <code>OutputStream</code> has not been closed for the <code>SQLXML</code>
     * object; if there is an error processing the XML value or the result set
     * concurrency is <code>CONCUR_READ_ONLY</code>. The <code>getCause</code>
     * method of the exception may provide a more detailed exception, for example,
     * if the stream does not contain valid XML.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        resultSet.updateSQLXML(columnIndex, xmlObject);
    }

    /**
     * Updates the designated column with a <code>java.sql.SQLXML</code> value. The
     * updater methods are used to update column values in the current row or the
     * insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param xmlObject the column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; this method is called on a closed result set; the
     * <code>java.xml.transform.Result</code>, <code>Writer</code> or
     * <code>OutputStream</code> has not been closed for the <code>SQLXML</code>
     * object; if there is an error processing the XML value or the result set
     * concurrency is <code>CONCUR_READ_ONLY</code>. The <code>getCause</code>
     * method of the exception may provide a more detailed exception, for example,
     * if the stream does not contain valid XML.
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        resultSet.updateSQLXML(columnLabel, xmlObject);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language. It is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return the column value; if the value is SQL <code>NULL</code>, the value
     * returned is <code>null</code>
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public String getNString(int columnIndex) throws SQLException {
        return resultSet.getNString(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>String</code> in the Java
     * programming language. It is intended for use when accessing
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @return the column value; if the value is SQL <code>NULL</code>, the value
     * returned is <code>null</code>
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public String getNString(String columnLabel) throws SQLException {
        return resultSet.getNString(columnLabel);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It is
     * intended for use when accessing <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return a <code>java.io.Reader</code> object that contains the column value;
     * if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language.
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        return resultSet.getNCharacterStream(columnIndex);
    }

    /**
     * Retrieves the value of the designated column in the current row of this
     * <code>ResultSet</code> object as a <code>java.io.Reader</code> object. It is
     * intended for use when accessing <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @return a <code>java.io.Reader</code> object that contains the column value;
     * if the value is SQL <code>NULL</code>, the value returned is
     * <code>null</code> in the Java programming language
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs or this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        return resultSet.getNCharacterStream(columnLabel);
    }

    /**
     * Updates the designated column with a character stream value, which will have
     * the specified number of bytes. The driver does the necessary conversion from
     * Java character format to the national character set in the database. It is
     * intended for use when updating <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        resultSet.updateNCharacterStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with a character stream value, which will have
     * the specified number of bytes. The driver does the necessary conversion from
     * Java character format to the national character set in the database. It is
     * intended for use when updating <code>NCHAR</code>,<code>NVARCHAR</code> and
     * <code>LONGNVARCHAR</code> columns.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader the <code>java.io.Reader</code> object containing the new
     * column value
     * @param length the length of the stream
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        resultSet.updateNCharacterStream(columnLabel, reader, length);
    }

    /**
     * Updates the designated column with an ascii stream value, which will have the
     * specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        resultSet.updateAsciiStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with a binary stream value, which will have the
     * specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        resultSet.updateBinaryStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with a character stream value, which will have
     * the specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        resultSet.updateCharacterStream(columnIndex, x, length);
    }

    /**
     * Updates the designated column with an ascii stream value, which will have the
     * specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        resultSet.updateAsciiStream(columnLabel, x, length);
    }

    /**
     * Updates the designated column with a binary stream value, which will have the
     * specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param x the new column value
     * @param length the length of the stream
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        resultSet.updateBinaryStream(columnLabel, x, length);
    }

    /**
     * Updates the designated column with a character stream value, which will have
     * the specified number of bytes.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader the <code>java.io.Reader</code> object containing the new
     * column value
     * @param length the length of the stream
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        resultSet.updateCharacterStream(columnLabel, reader, length);
    }

    /**
     * Updates the designated column using the given input stream, which will have
     * the specified number of bytes.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param inputStream An object that contains the data to set the parameter
     * value to.
     * @param length the number of bytes in the parameter data.
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        resultSet.updateBlob(columnIndex, inputStream, length);
    }

    /**
     * Updates the designated column using the given input stream, which will have
     * the specified number of bytes.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param inputStream An object that contains the data to set the parameter
     * value to.
     * @param length the number of bytes in the parameter data.
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        resultSet.updateBlob(columnLabel, inputStream, length);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object,
     * which is the given number of characters long. When a very large UNICODE value
     * is input to a <code>LONGVARCHAR</code> parameter, it may be more practical to
     * send it via a <code>java.io.Reader</code> object. The JDBC driver will do any
     * necessary conversion from UNICODE to the database char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        resultSet.updateClob(columnIndex, reader, length);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object,
     * which is the given number of characters long. When a very large UNICODE value
     * is input to a <code>LONGVARCHAR</code> parameter, it may be more practical to
     * send it via a <code>java.io.Reader</code> object. The JDBC driver will do any
     * necessary conversion from UNICODE to the database char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        resultSet.updateClob(columnLabel, reader, length);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object,
     * which is the given number of characters long. When a very large UNICODE value
     * is input to a <code>LONGVARCHAR</code> parameter, it may be more practical to
     * send it via a <code>java.io.Reader</code> object. The JDBC driver will do any
     * necessary conversion from UNICODE to the database char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @throws SQLException if the columnIndex is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set,
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        resultSet.updateNClob(columnIndex, reader, length);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object,
     * which is the given number of characters long. When a very large UNICODE value
     * is input to a <code>LONGVARCHAR</code> parameter, it may be more practical to
     * send it via a <code>java.io.Reader</code> object. The JDBC driver will do any
     * necessary conversion from UNICODE to the database char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader An object that contains the data to set the parameter value to.
     * @param length the number of characters in the parameter data.
     * @throws SQLException if the columnLabel is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        resultSet.updateNClob(columnLabel, reader, length);
    }

    /**
     * Updates the designated column with a character stream value. The data will be
     * read from the stream as needed until end-of-stream is reached. The driver
     * does the necessary conversion from Java character format to the national
     * character set in the database. It is intended for use when updating
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateNCharacterStream</code>
     * which takes a length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        resultSet.updateNCharacterStream(columnIndex, x);
    }

    /**
     * Updates the designated column with a character stream value. The data will be
     * read from the stream as needed until end-of-stream is reached. The driver
     * does the necessary conversion from Java character format to the national
     * character set in the database. It is intended for use when updating
     * <code>NCHAR</code>,<code>NVARCHAR</code> and <code>LONGNVARCHAR</code>
     * columns.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateNCharacterStream</code>
     * which takes a length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader the <code>java.io.Reader</code> object containing the new
     * column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        resultSet.updateNCharacterStream(columnLabel, reader);
    }

    /**
     * Updates the designated column with an ascii stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateAsciiStream</code> which
     * takes a length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        resultSet.updateAsciiStream(columnIndex, x);
    }

    /**
     * Updates the designated column with a binary stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateBinaryStream</code> which
     * takes a length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        resultSet.updateBinaryStream(columnIndex, x);
    }

    /**
     * Updates the designated column with a character stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateCharacterStream</code>
     * which takes a length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param x the new column value
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        resultSet.updateCharacterStream(columnIndex, x);
    }

    /**
     * Updates the designated column with an ascii stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateAsciiStream</code> which
     * takes a length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param x the new column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        resultSet.updateAsciiStream(columnLabel, x);
    }

    /**
     * Updates the designated column with a binary stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateBinaryStream</code> which
     * takes a length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param x the new column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        resultSet.updateBinaryStream(columnLabel, x);
    }

    /**
     * Updates the designated column with a character stream value. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateCharacterStream</code>
     * which takes a length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader the <code>java.io.Reader</code> object containing the new
     * column value
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        resultSet.updateCharacterStream(columnLabel, reader);
    }

    /**
     * Updates the designated column using the given input stream. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateBlob</code> which takes a
     * length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param inputStream An object that contains the data to set the parameter
     * value to.
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        resultSet.updateBlob(columnIndex, inputStream);
    }

    /**
     * Updates the designated column using the given input stream. The data will be
     * read from the stream as needed until end-of-stream is reached.
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateBlob</code> which takes a
     * length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param inputStream An object that contains the data to set the parameter
     * value to.
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        resultSet.updateBlob(columnLabel, inputStream);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object. The
     * data will be read from the stream as needed until end-of-stream is reached.
     * The JDBC driver will do any necessary conversion from UNICODE to the database
     * char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateClob</code> which takes a
     * length parameter.
     *
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param reader An object that contains the data to set the parameter value to.
     * @throws SQLException if the columnIndex is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        resultSet.updateClob(columnIndex, reader);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object. The
     * data will be read from the stream as needed until end-of-stream is reached.
     * The JDBC driver will do any necessary conversion from UNICODE to the database
     * char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateClob</code> which takes a
     * length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader An object that contains the data to set the parameter value to.
     * @throws SQLException if the columnLabel is not valid; if a database access
     * error occurs; the result set concurrency is <code>CONCUR_READ_ONLY</code> or
     * this method is called on a closed result set
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        resultSet.updateClob(columnLabel, reader);
    }

    /**
     * Updates the designated column using the given <code>Reader</code>
     * <p/>
     * The data will be read from the stream as needed until end-of-stream is
     * reached. The JDBC driver will do any necessary conversion from UNICODE to the
     * database char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateNClob</code> which takes a
     * length parameter.
     *
     * @param columnIndex the first column is 1, the second 2, ...
     * @param reader An object that contains the data to set the parameter value to.
     * @throws SQLException if the columnIndex is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set,
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        resultSet.updateNClob(columnIndex, reader);
    }

    /**
     * Updates the designated column using the given <code>Reader</code> object. The
     * data will be read from the stream as needed until end-of-stream is reached.
     * The JDBC driver will do any necessary conversion from UNICODE to the database
     * char format.
     * <p/>
     * <p/>
     * The updater methods are used to update column values in the current row or
     * the insert row. The updater methods do not update the underlying database;
     * instead the <code>updateRow</code> or <code>insertRow</code> methods are
     * called to update the database.
     * <p/>
     * <P>
     * <B>Note:</B> Consult your JDBC driver documentation to determine if it might
     * be more efficient to use a version of <code>updateNClob</code> which takes a
     * length parameter.
     *
     * @param columnLabel the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the
     * column
     * @param reader An object that contains the data to set the parameter value to.
     * @throws SQLException if the columnLabel is not valid; if the driver does not
     * support national character sets; if the driver can detect that a data
     * conversion error could occur; this method is called on a closed result set;
     * if a database access error occurs or the result set concurrency is
     * <code>CONCUR_READ_ONLY</code>
     * @throws SQLFeatureNotSupportedException if the JDBC driver does not support
     * this method
     * @since 1.6
     */
    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        resultSet.updateNClob(columnLabel, reader);
    }

    /**
     * Returns an object that implements the given interface to allow access to
     * non-standard methods, or standard methods not exposed by the proxy.
     * <p/>
     * If the receiver implements the interface then the result is the receiver or a
     * proxy for the receiver. If the receiver is a wrapper and the wrapped object
     * implements the interface then the result is the wrapped object or a proxy for
     * the wrapped object. Otherwise return the the result of calling
     * <code>unwrap</code> recursively on the wrapped object or a proxy for that
     * result. If the receiver is not a wrapper and does not implement the
     * interface, then an <code>SQLException</code> is thrown.
     *
     * @param iface A Class defining an interface that the result must implement.
     * @return an object that implements the interface. May be a proxy for the
     * actual implementing object.
     * @throws java.sql.SQLException If no object found that implements the
     * interface
     * @since 1.6
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }

        return resultSet.unwrap(iface);
    }

    /**
     * Returns true if this either implements the interface argument or is directly
     * or indirectly a wrapper for an object that does. Returns false otherwise. If
     * this implements the interface then return true, else if this is a wrapper
     * then return the result of recursively calling <code>isWrapperFor</code> on
     * the wrapped object. If this does not implement the interface and is not a
     * wrapper, return false. This method should be implemented as a low-cost
     * operation compared to <code>unwrap</code> so that callers can use this method
     * to avoid expensive <code>unwrap</code> calls that may fail. If this method
     * returns true then calling <code>unwrap</code> with the same argument should
     * succeed.
     *
     * @param iface a Class defining an interface.
     * @return true if this implements the interface or directly or indirectly wraps
     * an object that does.
     * @throws java.sql.SQLException if an error occurs while determining whether
     * this is a wrapper for an object with the given interface.
     * @since 1.6
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return true;
        }

        return resultSet.isWrapperFor(iface);
    }
}
