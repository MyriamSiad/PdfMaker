package fr.pdfmaker.backend.mongo.document;

import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;



@Getter  @Setter  @ToString
@Document(collection = "loginHistorique")
public class LoginHistorique {


    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    private String firstname;
    private String lastname;

    private String email;

    @Field("login_date")
    private LocalDateTime loginDate;


    @Enumerated
    private String status; //SUCCESS, FAILURE
}
