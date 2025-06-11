package com.grepp.nbe562team04.model.goalcompany.code;

import lombok.Getter;

@Getter
public enum GoalStatus {

    DOCUMENT("서류 마감"),
    CODING_TEST("코딩테스트"),
    ASSIGNMENT("과제"),
    APTITUDE("인적성검사"),
    INTERVIEW_1("1차 면접"),
    INTERVIEW_2("2차 면접"),
    INTERVIEW_3("3차 면접 (컬처핏)");

    private final String label;

    GoalStatus(String label) {
        this.label = label;
    }
}