import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.Collection;

public interface Generator {

    private TypeSpec spec;

    default Collection<?> generate() {
        return JavaFile.builder().build()
    }

    default

}
