package com.s4.demo.domain.search;

import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class SpecificationsBuilder<T> {

    private final List<SearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public SpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public org.springframework.data.jpa.domain.Specification build() {
        if (params.size() == 0) {
            return null;
        }

        List<org.springframework.data.jpa.domain.Specification> specs = new ArrayList<org.springframework.data.jpa.domain.Specification>();
        for (SearchCriteria param : params) {
            specs.add(new Specification(param));
        }

        org.springframework.data.jpa.domain.Specification result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }

}
