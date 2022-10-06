package com.example.profile.dto;

import com.example.profile.annotation.MinAgeDOB;
import com.example.profile.annotation.NotAllNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NotAllNull
public class ProfileDto {
    @NotBlank(message = "email cannot be blank")
    @Email(message = "invalid email")
    private String email;

    @Size(min = 1, message = "atleast one character in name")
    @Size(max = 31, message = "atmost 31 characters in name")
    @Pattern(regexp = "[a-z\\sa-z]+", message = "name should have alphabets and blank spaces only")
    private String name;

    @Past(message = "DOB has to be in past")
    @MinAgeDOB(minimumAge = 16)
    private LocalDate dob;

    @Pattern(regexp = "^\\d{10}$", message = "number should be only 10 digits")
    private String number;
}
