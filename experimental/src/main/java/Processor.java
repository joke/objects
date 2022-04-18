import com.squareup.javapoet.MethodSpec;

import java.util.Collection;
import java.util.Collections;

public class Processor implements CatTrait, DogTrait {

    public Collection<MethodSpec> generate() {
        return Collections.emptyList();
    }

    public static void main(String... args) {
        new Processor().generate();
    }

    @Override
    public Collection<MethodSpec> generateMethods() {
        return CatTrait.super.generateMethods();
    }
}
