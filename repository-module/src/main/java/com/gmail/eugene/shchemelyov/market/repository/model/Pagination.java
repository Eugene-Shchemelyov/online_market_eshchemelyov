package com.gmail.eugene.shchemelyov.market.repository.model;

import com.gmail.eugene.shchemelyov.market.repository.model.enums.SortEnum;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.eugene.shchemelyov.market.repository.constant.PaginationConstant.LIMIT_ON_PAGE;

public class Pagination<T> {
    private Integer currentPage;
    private Integer countPages;
    private Integer limitOnPage = LIMIT_ON_PAGE;
    private Integer startLimitPosition;
    private List<T> entities = new ArrayList<>();
    private SortEnum sort;
    private boolean isDeleted = false;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getCountPages() {
        return countPages;
    }

    public void setCountPages(Integer countPages) {
        this.countPages = countPages;
    }

    public Integer getLimitOnPage() {
        return limitOnPage;
    }

    public void setLimitOnPage(Integer limitOnPage) {
        this.limitOnPage = limitOnPage;
    }

    public Integer getStartLimitPosition() {
        return startLimitPosition;
    }

    public void setStartLimitPosition(Integer startLimitPosition) {
        this.startLimitPosition = startLimitPosition;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public SortEnum getSort() {
        return sort;
    }

    public void setSort(SortEnum sort) {
        this.sort = sort;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
