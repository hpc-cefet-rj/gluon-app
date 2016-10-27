package com.gluonhq.codevault.git;

public class Topic extends GitRef {

    private final String name;

    public Topic(String name) {
        super(null);
        this.name = name;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String getShortName() {
        return name;
    }

    @Override
    public String getId() {
        return "";
    }
}
