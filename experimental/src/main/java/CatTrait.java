import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.Collection;
import java.util.Collections;

interface CatTrait {

    default Collection<MethodSpec> generateMethods() {
        final MethodSpec cat = MethodSpec.methodBuilder("cat")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", "meow")
                .build();
        return Collections.singleton(cat);
    }

}
