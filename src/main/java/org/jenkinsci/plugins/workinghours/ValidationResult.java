package org.jenkinsci.plugins.workinghours;

import net.sf.json.JSONObject;

public class ValidationResult {
    public boolean isValid;
    public String errorMessage;
    public String fieldName;

    public ValidationResult(boolean isValid, String field, String errorMessage) {
        this.isValid = isValid;
        this.fieldName = field;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult getSuccessValidation() {
        return new ValidationResult(true, "", "");
    }

    public JSONObject toJSON() {
        return new JSONObject().accumulate("valid", true)
            .accumulate("field",this.fieldName)
            .accumulate("errorMessage",this.errorMessage);
    }
}
