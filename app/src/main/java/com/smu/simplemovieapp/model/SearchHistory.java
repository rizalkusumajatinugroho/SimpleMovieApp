package com.smu.simplemovieapp.model;

/**
 * Created by sapuser on 12/9/2018.
 */

public class SearchHistory {
    private String query;
    private String page;
    private String numPage;
    private String flagDone;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getNumPage() {
        return numPage;
    }

    public void setNumPage(String numPage) {
        this.numPage = numPage;
    }

    public String getFlagDone() {
        return flagDone;
    }

    public void setFlagDone(String flagDone) {
        this.flagDone = flagDone;
    }
}
