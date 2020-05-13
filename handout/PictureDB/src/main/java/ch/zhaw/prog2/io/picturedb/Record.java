package ch.zhaw.prog2.io.picturedb;

public abstract class Record {
    private static final String DEFAULT_ID = "-1";
    /**
     * Identifier of the record.
     */
    protected String id = DEFAULT_ID;

    /**
     * Set the id for this record. This method is used by the specific data set handler
     * and should not be used directly by the user.
     * @param id new id of the record, when the record is added to the dataset
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the record
     * @return identifier of the record
     */
    public String getId() {
        return id;
    }

    public boolean isNew() {
        return id.equals(DEFAULT_ID);
    }

}
