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
@Table(name = "screening")
public class ScreeningEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private Integer screeningId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "staff")
    private String staff ;

    @Column(name = "screen_date")
    private String screenDate;

    @Column(name = "screen_time")
    private String screenTime;

    @Column(name = "screening_questions")
    private String screeningQuestions;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_mobile")
    private String userMobile;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "user_temperature")
    private String userTemperature;

    @Column(name = "temperature_image_url")
    private String temperatureImageUrl;
}




