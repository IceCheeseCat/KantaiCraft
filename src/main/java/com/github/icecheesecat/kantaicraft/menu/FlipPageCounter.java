package com.github.icecheesecat.kantaicraft.menu;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.function.Predicate;

public class FlipPageCounter {

    int pageNumber;
    int pageSize;

    public FlipPageCounter(int pageSize) {
        this(0, pageSize);
    }

    public FlipPageCounter(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    protected boolean pageReachedEnd(int elementSize) {
        return elementSize <= pageSize * pageNumber;
    }

    protected boolean pageReachedHead() {
        return this.pageNumber < 0;
    }

    public void flipToNextPage(int elementSize) {
        this.pageNumber++;
        if (pageReachedEnd(elementSize)) {
            this.pageNumber = 0;
        }
    }

    public void flipToPrevPage(int elementSize) {
        this.pageNumber--;
        if (pageReachedHead()) {
            this.pageNumber = calculateEndPage(elementSize);
        }
    }
    
    private int calculateEndPage(int elementSize) {
        return elementSize == 0 ? 0 : (elementSize % pageSize == 0) ? elementSize / pageSize - 1 : elementSize / pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalPageNumber(int elementSize) {
        return calculateEndPage(elementSize);
    }

    public <T> List<T> evaluatePageElements(List<T> inputs) {
        List<T> returnEle = new ArrayList<>();
        for (int i = this.getPageNumber() * pageSize; i < inputs.size() && i < this.getPageNumber() * this.pageSize + this.pageSize; i++) {
            returnEle.add(inputs.get(i));
        }

        return returnEle;
    }

}
