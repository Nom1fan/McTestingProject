package framework.infra.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mor on 14/07/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegerProperty {
    String property();
}
