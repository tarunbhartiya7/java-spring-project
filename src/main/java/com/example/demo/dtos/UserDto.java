package com.example.demo.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

// @JsonIgnore to ignore the field during serialization
// @JsonProperty("user_id") to rename the field during serialization
// @JsonFormat(pattern = 'yyyy-MM-dd HH:mm:ss') to format the date
@AllArgsConstructor
@Getter
public class UserDto {
    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;
}
