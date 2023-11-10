package com.example.servertoserverdat22c.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class Gender {
        private int count;
        private String name;
        private String gender;
        private double probability;
}
