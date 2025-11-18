package com.vitalioleksenko.csp.validation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

//TODO
@Component
public class CheckUniqueValueValidator implements ConstraintValidator<UniqueValue, String> {

    @PersistenceContext
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
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        try {
            String queryStr = "SELECT COUNT(e) FROM " + entityClass.getSimpleName()
                    + " e WHERE e." + fieldName + " = :value";

            Long count = entityManager.createQuery(queryStr, Long.class)
                    .setParameter("value", value.trim())
                    .getSingleResult();

            return count == 0;
        } catch (Exception e) {
            return false;
        }
    }
}