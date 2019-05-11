package com.gmail.eugene.shchemelyov.market.repository.model;

import static com.gmail.eugene.shchemelyov.market.repository.constant.PaginationConstant.LIMIT_ON_PAGE;

public class Pagination {
    private Integer currentPage;
    private Integer countPages;
    private Integer limitOnPage = LIMIT_ON_PAGE;
    private Integer startLimitPosition;
    private Boolean isDeleted = false;

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

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
