package com.zuzex.vvolkov.constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseMapper {

    @ApiModelProperty("Status of response, like http status")
    public final Integer status;
    @ApiModelProperty("Additional message with error/success message")
    public final String message;
    @ApiModelProperty("Response model of required entity")
    public final Object body;
}
