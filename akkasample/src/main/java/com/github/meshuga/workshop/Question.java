package com.github.meshuga.workshop;

public class Question {
    private String value;

    public String getValue() {
        return value;
    }

    public Question setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return value != null ? value.equals(question.value) : question.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
