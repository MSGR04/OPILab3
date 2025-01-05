package org.web.lab3.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.web.lab3.models.Result;
import org.web.lab3.utils.DatabaseManager;

import java.io.Serializable;
import java.util.List;

@Named
@ApplicationScoped
public class ResultTableBean implements Serializable {

    private List<Result> results;

    public List<Result> getResults() {
        loadResults();
        return results;
    }

    private void loadResults() {
        this.results = DatabaseManager.fetchResults();
    }
}
