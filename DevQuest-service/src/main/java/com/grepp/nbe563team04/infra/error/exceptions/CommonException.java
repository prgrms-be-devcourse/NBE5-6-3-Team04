package com.grepp.nbe563team04.infra.error.exceptions;

import com.grepp.nbe563team04.infra.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonException extends RuntimeException {
    
    private final ResponseCode code;
    private String redirect = "/";
    
    public CommonException(ResponseCode code) {
        this.code = code;
    }
    
    public CommonException(ResponseCode code, String redirect) {
        this.code = code;
        this.redirect = redirect;
    }
    
    public String redirect(){return redirect; }
    public ResponseCode code() {
        return code;
    }
}
