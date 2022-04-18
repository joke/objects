import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.Collections;

interface DogTrait {

    default Collection<MethodSpec> generateMethods() {
        final MethodSpec whoof = MethodSpec.methodBuilder("dog")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", "woof")
                .build();
        return Collections.singleton(whoof);
    }

}
