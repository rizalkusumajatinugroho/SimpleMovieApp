package com.smu.simplemovieapp.model;

/**
 * Created by sapuser on 12/8/2018.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieResource {

    @SerializedName("Search")
    @Expose
    private List<MovieHeader> search = null;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;
    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("Error")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<MovieHeader> getSearch() {
        return search;
    }

    public void setSearch(List<MovieHeader> search) {
        this.search = search;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
