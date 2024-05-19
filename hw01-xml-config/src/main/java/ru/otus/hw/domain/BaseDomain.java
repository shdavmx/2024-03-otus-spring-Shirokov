package ru.otus.hw.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDomain {
    private boolean isFailed;
    private String errorText;
}
