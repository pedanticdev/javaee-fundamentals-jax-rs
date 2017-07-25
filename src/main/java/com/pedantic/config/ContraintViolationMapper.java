package com.pedantic.config;

import com.pedantic.entities.ConstraintViolationDAO;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ContraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation vi : e.getConstraintViolations()) {
            String propertyName = vi.getPropertyPath().toString().split("\\.")[2];
            errors.put(propertyName, vi.getMessage());
            
        }

        return Response.status(Response.Status.PRECONDITION_FAILED)
                .entity(new ConstraintViolationDAO(errors)).build();
    }
}
