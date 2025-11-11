package com.vitalioleksenko.csp.validation;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class CheckUniqueValueValidator implements ConstraintValidator<UniqueValue, String> {
    @Autowired
    private EntityManager entityManager;

    private String fieldName;
    private Class<?> entityClass;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String queryStr = "SELECT COUNT(e) FROM " + entityClass.getSimpleName()
                + " e WHERE e." + fieldName + " = :value";
        Long count = entityManager.createQuery(queryStr, Long.class)
                .setParameter("value", value)
                .getSingleResult();
        return count == 0;
    }
}
