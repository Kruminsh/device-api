package com.device.api.model.response;

import java.util.ArrayList;
import java.util.List;

public class RegisterResponse extends CommonResponse {
    private boolean success;

    public RegisterResponse(boolean success) {
        super(new ArrayList<>());
        this.success = success;
    }

    public RegisterResponse(boolean success, List<String> errors) {
        super(errors);
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }
}
