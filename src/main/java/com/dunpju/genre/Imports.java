package com.dunpju.genre;

import java.util.ArrayList;
import java.util.List;

public class Imports {
    private final List<String> value = new ArrayList<>();

    public Imports append(String value) {
        this.value.add(value);
        return this;
    }

    @Override
    public String toString() {
        return String.join("\n", this.value);
    }
}
