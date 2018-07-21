package com.dialog.edit.label;

import java.io.Serializable;

public class LabelEntry implements Serializable {

    private String content;
    private int number;
    private boolean isChoice;

    public LabelEntry(String content, boolean isChoice) {
        this.content = content;
        this.isChoice = isChoice;
    }

    public LabelEntry(String content, boolean isChoice, int number) {
        this.content = content;
        this.isChoice = isChoice;
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "LabelEntry{" +
                "content='" + content + '\'' +
                ", number=" + number +
                ", isChoice=" + isChoice +
                '}';
    }
}
