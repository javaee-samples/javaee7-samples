package org.javaee7.jca.connector.simple.connector.cci;

import javax.resource.cci.Record;

/**
 *
 * @author arungup
 */
public class MyResponseRecord implements Record {

    private String data;
    private String name;
    private String description;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getRecordName() {
        return name;
    }

    @Override
    public void setRecordName(String name) {
        this.name = name;
    }

    @Override
    public void setRecordShortDescription(String description) {
        this.description = description;
    }

    @Override
    public String getRecordShortDescription() {
        return description;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();

        MyOrderRecord record = new MyOrderRecord();
        record.setData(this.data);
        record.setRecordName(this.name);
        record.setRecordShortDescription(this.description);
        return record;
    }

}
