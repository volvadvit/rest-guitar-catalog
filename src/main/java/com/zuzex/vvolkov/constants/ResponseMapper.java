package com.zuzex.vvolkov.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseMapper {

    public final Integer status;
    public final String message;
    public final Object body;
}
