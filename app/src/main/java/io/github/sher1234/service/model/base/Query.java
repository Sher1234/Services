package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Query implements Serializable {

    private String Query = "";
    private String Table = "";

    public Query() {

    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getTable() {
        return Table;
    }

    public void setTable(String table) {
        Table = table;
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Query", getQuery());
        map.put("Table", getTable());
        return map;
    }
}
