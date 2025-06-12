package com.grepp.nbe563team04.model.goal.code;

import lombok.Getter;

@Getter
public enum GoalList {

    DOCUMENT("서류"),
    CODING_TEST("코딩테스트"),
    ASSIGNMENT("과제"),
    APTITUDE("인적성검사");


    private final String label;

    GoalList(String label) {
        this.label = label;
    }
}