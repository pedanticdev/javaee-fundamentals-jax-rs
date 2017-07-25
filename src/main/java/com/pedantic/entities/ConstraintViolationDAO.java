package com.pedantic.entities;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;


@XmlRootElement
public class ConstraintViolationDAO {

    private Map<String, String> errors;

    public ConstraintViolationDAO() {
    }

    public ConstraintViolationDAO(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
