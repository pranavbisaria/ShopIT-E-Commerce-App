package com.ShopIT.Payloads;

import com.ShopIT.Models.Images;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Integer Id;
    private Set<Images> images = new HashSet<>(0);
    @NotNull(message = "rating field required")
    @Pattern(regexp = "^[1-5]$", message = "The entered value should be ranging from 1 to 5, and should be integer")
    private String rating;
    private String description;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date issueTime;
    private UserShow user;
}
