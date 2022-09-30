package com.example.profile.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Profile {
    @Id
    private Integer id;
    private String email;
    private String password;
    private String name;
    private LocalDate dob;
    private String number;
}
