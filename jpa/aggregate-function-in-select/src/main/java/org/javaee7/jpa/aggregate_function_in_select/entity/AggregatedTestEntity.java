package org.javaee7.jpa.aggregate_function_in_select.entity;

/**
 * A very simple DTO that will be used to aggregate TestEnity to
 * 
 * @author Arjan Tijms
 *
 */
public class AggregatedTestEntity {

    private String values;

    public AggregatedTestEntity(String values) {
        this.values = values;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

}
