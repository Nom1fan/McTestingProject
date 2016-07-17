package framework.infra.config;

import framework.infra.annotations.RandomString;
import framework.infra.data.StringGenerator;

import java.lang.reflect.Field;
import java.util.Random;

/**
 * Created by Mor on 15/07/2016.
 */
public class RandomStringConfigurator implements ObjectConfigurator {
    @Override
    public void configure(Object o) throws Exception {
        Class type = o.getClass();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(RandomString.class)) {
                RandomString anno = field.getAnnotation(RandomString.class);
                int size = anno.size();
                if(size==0)
                    size = new Random().nextInt();
                String val = StringGenerator.getRandomGeneratedString(size);
                field.setAccessible(true);
                field.set(o, val);
            }
        }
    }
}
