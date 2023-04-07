package com.sam.coin.model;

import java.sql.Timestamp;

public class TableInfo {
    private int count;
    private Timestamp mostRecent;

    public TableInfo(int count, Timestamp mostRecent) {
        this.count = count;
        this.mostRecent = mostRecent;
    }

    public int getCount() {
        return count;
    }

    public Timestamp getMostRecent() {
        return mostRecent;
    }
}