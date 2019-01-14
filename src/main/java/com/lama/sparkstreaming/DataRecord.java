package com.lama.sparkstreaming;

import java.io.Serializable;

public class DataRecord implements Serializable {
    private long COL3;
    private String COL5;
    private int COL8;

    public long getCOL3() {
        return COL3;
    }

    public void setCOL3(long COL3) {
        this.COL3 = COL3;
    }

    public String getCOL5() {
        return COL5;
    }

    public void setCOL5(String COL5) {
        this.COL5 = COL5;
    }

    public int getCOL8() {
        return COL8;
    }

    public void setCOL8(int COL8) {
        this.COL8 = COL8;
    }
}
