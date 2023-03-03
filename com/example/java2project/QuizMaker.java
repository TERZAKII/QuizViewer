package com.example.java2project;

public class QuizMaker {
    public QuizMaker() {
    }

    public static void main(String[] args) throws Exception {
        Quiz var1 = Quiz.loadFromFile("D:\\JavaProjects\\Java2Project\\src\\main\\java\\com\\example\\java2project\\JavaQuiz.txt");
        if (var1 != null) {
            var1.start();
        }

    }
}