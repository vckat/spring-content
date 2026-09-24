package org.springframework.content.commons.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.annotation.AnnotationUtils;

public final class IdAnnotationUtils {

    private static final String DATA_ID_ANNOTATION = "org.springframework.data.annotation.Id";
    private static final String JAKARTA_ID_ANNOTATION = "jakarta.persistence.Id";
    private static final String JAVAX_ID_ANNOTATION = "javax.persistence.Id";
    private static final List<Class<? extends Annotation>> ID_ANNOTATION_TYPES = loadIdAnnotationTypes();

    private IdAnnotationUtils() {
    }

    public static boolean isIdAnnotation(Annotation annotation) {

        if (annotation == null) {
            return false;
        }

        String annotationName = annotation.annotationType().getCanonicalName();
        return DATA_ID_ANNOTATION.equals(annotationName)
                || JAKARTA_ID_ANNOTATION.equals(annotationName)
                || JAVAX_ID_ANNOTATION.equals(annotationName);
    }

    public static Object getId(Object entity) {

        for (Class<? extends Annotation> annotationType : ID_ANNOTATION_TYPES) {
            if (BeanUtils.hasFieldWithAnnotation(entity, annotationType)) {
                Object id = BeanUtils.getFieldWithAnnotation(entity, annotationType);
                if (id != null) {
                    return id;
                }

                PropertyDescriptor[] propertyDescriptors = new BeanWrapperImpl(entity).getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getReadMethod() != null
                            && AnnotationUtils.findAnnotation(propertyDescriptor.getReadMethod(), annotationType) != null) {
                        return new BeanWrapperImpl(entity).getPropertyValue(propertyDescriptor.getName());
                    }
                }
            }
        }

        return null;
    }

    public static Field getIdField(Class<?> domainClass) {

        for (Class<? extends Annotation> annotationType : ID_ANNOTATION_TYPES) {
            Field field = BeanUtils.findFieldWithAnnotation(domainClass, annotationType);
            if (field != null) {
                return field;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static List<Class<? extends Annotation>> loadIdAnnotationTypes() {

        List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();
        annotationTypes.add(org.springframework.data.annotation.Id.class);
        addIfPresent(annotationTypes, JAKARTA_ID_ANNOTATION);
        addIfPresent(annotationTypes, JAVAX_ID_ANNOTATION);
        return annotationTypes;
    }

    @SuppressWarnings("unchecked")
    private static void addIfPresent(List<Class<? extends Annotation>> annotationTypes, String className) {

        try {
            annotationTypes.add((Class<? extends Annotation>) IdAnnotationUtils.class.getClassLoader().loadClass(className));
        }
        catch (ClassNotFoundException e) {
        }
    }
}
