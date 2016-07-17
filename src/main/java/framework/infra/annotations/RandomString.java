package framework.infra.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mor on 15/07/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomString {
    int size();
}
