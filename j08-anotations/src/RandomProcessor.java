import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandomProcessor {
    private final static SecureRandom secureRandom = new SecureRandom();
    private static Map<Type, FieldSetter> mapi = new HashMap<>();

    static {
        mapi.put(Integer.TYPE, (Object o, Field f, Randomize r) -> {
            try {
                f.set(o, r.low() + secureRandom.nextInt(r.high() - r.low()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        mapi.put(Double.TYPE, (Object o, Field f, Randomize r) -> {
            try {
                f.set(o, r.low() + secureRandom.nextDouble(r.high() - r.low()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        mapi.put(Long.TYPE, (Object o, Field f, Randomize r) -> {
            try {
                f.set(o, r.low() + secureRandom.nextLong(r.high() - r.low()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        mapi.put(String.class, (Object o, Field f, Randomize r) -> {
            try {
                var sb = new StringBuilder(r.length());
                for(int i = 0; i < r.length(); i++) {
                    sb.append((char) (33 + secureRandom.nextInt(93)));
                }
                f.set(o, sb.toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void processor(Klass toProcess) {
        var klass = toProcess.getClass();
        Field[] fields = klass.getDeclaredFields();
        List publicFields = Arrays.stream(klass.getFields()).toList();
        for(Field field : fields) {
            var randomize = field.getAnnotation(Randomize.class);
            if(randomize != null) {
                if(! publicFields.contains(field)) {
                    field.setAccessible(true);
                }
                if(mapi.containsKey(field.getType())) {
                    mapi.get(field.getType()).set(toProcess, field, randomize);
                }
                if(! publicFields.contains(field)) {
                    field.setAccessible(false);
                }
            }
        }
    }
}
