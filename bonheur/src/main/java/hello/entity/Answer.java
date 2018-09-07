package hello.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long answerId;

    private String answer;

    private Long questionId;

    public Answer() {
    }

    public Answer(String answer, Long questionId) {
        this.answer = answer;
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
