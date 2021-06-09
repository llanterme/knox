package za.co.knox.restservice.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "screening_questions")
public class ScreeningQuestionsEntity {

    @Id
    @Setter
    @GeneratedValue
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "question")
    private String question;

    @Column(name = "question_identifier")
    private String questionIdentifier;


}


