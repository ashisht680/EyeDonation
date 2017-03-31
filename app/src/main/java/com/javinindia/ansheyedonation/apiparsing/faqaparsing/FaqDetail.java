package com.javinindia.ansheyedonation.apiparsing.faqaparsing;

/**
 * Created by Ashish on 30-03-2017.
 */

public class FaqDetail {
    private String id;
    private String question;
    private String answer;
    private String date;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
