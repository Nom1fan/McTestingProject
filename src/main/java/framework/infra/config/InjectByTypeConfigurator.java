package framework.infra.config;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import framework.infra.ObjectFactory;
import framework.infra.annotations.InjectByType;
import framework.infra.annotations.IntegerProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Mor on 15/07/2016.
 */
public class InjectByTypeConfigurator implements ObjectConfigurator {

    private Config config = new JavaConfig();

    @Override
    public void configure(Object o) throws Exception {
        Iterable<Field> fields = getFieldsUpTo(o.getClass(), Object.class);
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                Class<?> type = field.getType();
                Object value = ObjectFactory.getInstance().createObject(type);
                field.setAccessible(true);
                field.set(o,value);
            }
        }
    }

    public Iterable<Field> getFieldsUpTo(@Nonnull Class<?> startClass,
                                                @Nullable Class<?> exclusiveParent) {

        List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());
        Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null &&
                (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {
            List<Field> parentClassFields =
                    (List<Field>) getFieldsUpTo(parentClass, exclusiveParent);
            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;
    }
}
