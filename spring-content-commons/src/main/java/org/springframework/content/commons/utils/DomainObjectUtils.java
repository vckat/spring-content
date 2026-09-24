package org.springframework.content.commons.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public final class DomainObjectUtils {

    private DomainObjectUtils() {}

    public static final Object getId(Object entity) {
        return IdAnnotationUtils.getId(entity);
    }

    public static final Field getIdField(Class<?> domainClass) {
        return IdAnnotationUtils.getIdField(domainClass);
    }

    public static boolean isIdAnnotation(Annotation annotation) {
        return IdAnnotationUtils.isIdAnnotation(annotation);
    }
}
