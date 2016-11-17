package com.polycom.honeycomb.rm.utils;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Chen on 2016/11/17.
 * <p>
 * This is the POJO class especially for org.springframework.data.domain.Page
 */
public class PageImplBean<T> extends PageImpl<T> {

    private static final long serialVersionUID = 1L;
    private int     number;
    private int     size;
    private int     totalPages;
    private int     numberOfElements;
    private long    totalElements;
    private boolean first;
    private boolean nextPage;
    private boolean last;
    private List<T> content;
    private Sort    sort;

    public PageImplBean() {
        super(new ArrayList<T>());
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        //The sise for page request has to be at least 1
        //Otherwise the deserialization will be failed
        if (size < 1) {
            size = 1;
        }
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public PageImpl<T> pageImpl() {
        return new PageImpl<T>(getContent(),
                               new PageRequest(getNumber(),
                                               getSize(),
                                               getSort()),
                               getTotalElements());
    }
}
