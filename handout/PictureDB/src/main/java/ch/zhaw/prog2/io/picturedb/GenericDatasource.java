package ch.zhaw.prog2.io.picturedb;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

/**
 * Generic data store interface to persist items of type T
 * What kind of persistence media is used is defined by the concrete implementation.
 * e.g. InMemory, Files, Database, ...
 * The datatype T to be persisted must have an id field of type int
 * to uniqely identify the record.
 *
 * @param <T extends Record> Record data type to persist
 */
public interface GenericDatasource<T extends Record> {
    /**
     * Insert a new record to the data store.
     * The id field of the record is ignored, and a new unique id is generated,
     * which will be set in the record.
     * This id is used to identify the record in the dataset by the other methods
     * (i.e. find, update or delete methods)
     *
     * @param record of type T to insert into the data set.
     */
    public void insert(T record) throws IOException;

    /**
     * Update the content of an existing record in the data set,
     * which is identified by the unique identifier,
     * with the new values from the given record object.
     * If the identifier can not be found in the data set,
     * an {@link RecordNotFoundException} is thrown.
     *
     * @param record to be updated in the dataset
     * @throws RecordNotFoundException if the record is not existing
     */
    public void update(T record) throws RecordNotFoundException, IOException;

    /**
     * Deletes the record, identified by the id of the given record from the data set.
     * All other fields of he record are ignored.
     * If the identifier can not be found in the data set,
     * an {@link RecordNotFoundException} is thrown.
     *
     * @param record to be deleted
     * @throws RecordNotFoundException if the record is not existing
     */
    public void delete(T record) throws RecordNotFoundException, IOException;

    /**
     * Returns the number of records in the data set
     * @return number of records
     */
    public int count() throws IOException;

    /**
     * Retrieves an instance of the record identified by the given id.
     * If the record can not be found, null is returnd
     * @param id of the record to be retrieved
     * @return record of type T or null if not found
     */
    public T findById(String id) throws IOException, ParseException, RecordNotFoundException;

    /**
     * Retrieves all records of the data set
     * @return collection of all records of the data set
     */
    public Collection<T> findAll() throws IOException, ParseException;
}
