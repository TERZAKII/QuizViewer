package com.example.java2project;

class Student implements Comparable<Student> {
    private int id;
    private String name;

    Student(int var1, String var2) {
        this.id = var1;
        this.name = var2;
    }

    public int compareTo(Student var1) {
        if (this.id > var1.id) {
            return 1;
        } else {
            return this.id == var1.id ? 0 : -1;
        }
    }

    public String toString() {
        return this.id + ": " + this.name;
    }
}
