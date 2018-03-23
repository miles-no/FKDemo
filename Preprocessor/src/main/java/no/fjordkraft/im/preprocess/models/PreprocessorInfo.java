package no.fjordkraft.im.preprocess.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by bhavi on 5/8/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PreprocessorInfo {
    String brand() default "";
    String legalPartClass() default "";
    int order();
   boolean skipOnline() default  false;
}
