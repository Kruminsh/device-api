package com.device.api.model.response;

import java.util.List;

public abstract class CommonResponse {
    private final List<String> errors;

    public CommonResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
