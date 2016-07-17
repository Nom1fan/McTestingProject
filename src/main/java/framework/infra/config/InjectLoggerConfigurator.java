package framework.infra.config;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import framework.infra.annotations.InjectLogger;
import framework.infra.annotations.IntegerProperty;
import framework.infra.logging.LogsManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Mor on 15/07/2016.
 */
public class InjectLoggerConfigurator implements ObjectConfigurator {
    @Override
    public void configure(Object o) throws Exception {
        Iterable<Field> fields = getFieldsUpTo(o.getClass(), Object.class);
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectLogger.class)) {
                InjectLogger anno = field.getAnnotation(InjectLogger.class);
                String name = anno.name();
                String folder = anno.folder();
                String logPath =
                        Paths.get("").toAbsolutePath().toString() +
                                "\\" + folder + "\\";
                new File(logPath).mkdirs();
                LogsManager.clearLogs(logPath);
                Logger logger = LogsManager.getLogger(
                       name,
                        logPath);
                field.setAccessible(true);
                field.set(o, logger);
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
