package com.example.maxi.unifundme;

/**
 * Created by Maxi on 2018-03-18.
 */

public class Award {

    private Integer id;
    private String source;
    private String type;
    private String name;
    private Double amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Award(Integer id, String source, String type, String name, double amount) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.name = name;
        this.amount = amount;
    }

}
