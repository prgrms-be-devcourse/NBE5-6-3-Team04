package com.grepp.nbe563team04.infra.error.exceptions;

import com.grepp.nbe563team04.infra.response.ResponseCode;

public class AuthApiException extends CommonException{
    
    public AuthApiException(ResponseCode code) {
        super(code);
    }
}
