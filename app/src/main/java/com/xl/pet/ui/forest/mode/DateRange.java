package com.xl.pet.ui.forest.mode;


public class DateRange {
    private long start;
    private long end;

    public DateRange() {
    }

    public DateRange(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public DateRange reset(long start, long end) {
        this.start = start;
        this.end = end;
        return this;
    }

    public void startAdd(long add) {
        start += add;
    }

    public void endAdd(long add) {
        end += add;
    }


    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
