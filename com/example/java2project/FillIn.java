package com.example.java2project;

class FillIn extends Question {
    FillIn() {
    }

    public String toString() {
        return this.getDescription().replace("{blank}", "_____");
    }
}
