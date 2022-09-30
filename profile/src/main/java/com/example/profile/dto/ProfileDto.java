package com.example.profile.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProfileDto {
    private String email;
    private String name;
    private LocalDate dob;
    private String number;
}
