package io.github.sher1234.service.model.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Query implements Serializable {

    public String Query;
    public String Table;

    public Query() {
        Query = "";
        Table = "";
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Query", Query);
        map.put("Table", Table);
        return map;
    }
}
