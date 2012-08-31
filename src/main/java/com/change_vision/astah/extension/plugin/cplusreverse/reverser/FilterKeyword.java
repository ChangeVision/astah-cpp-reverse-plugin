package com.change_vision.astah.extension.plugin.cplusreverse.reverser;

import java.util.Set;

public class FilterKeyword {
    public Set<String> keywords;
    public String toType;

    public FilterKeyword(Set<String> keywords, String toType) {
        super();
        this.keywords = keywords;
        this.toType = toType;
    }
}
