package org.jenkinsci.plugins.workinghours;

import net.sf.json.JSONObject;

public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;
    private final String fieldName;

    public ValidationResult(boolean valid, String field, String errorMessage) {
        this.valid = valid;
        this.fieldName = field;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.fieldName = "";
        this.errorMessage = "";
    }

    public static ValidationResult getSuccessValidation() {
        return new ValidationResult(true, "", "");
    }

    public JSONObject toJSON() {
        return new JSONObject().accumulate("valid", this.valid)
            .accumulate("field", this.fieldName)
            .accumulate("errorMessage", this.errorMessage);
    }

    public String toErrorMessage() {
        return this.fieldName + " " + this.errorMessage;
    }

    public boolean isValid(){
        return this.valid;
    }
}
