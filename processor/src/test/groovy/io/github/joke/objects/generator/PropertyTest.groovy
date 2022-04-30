package io.github.joke.objects.generator


import io.github.joke.objects.scanner.Property
import spock.lang.Specification
import spock.lang.Subject

import javax.lang.model.element.Element

import static com.squareup.javapoet.TypeName.BYTE

class PropertyTest extends Specification {

    Element origin = DeepMock()

    @Subject
    def property = new Property(origin, 'my-prop', true, BYTE)

    def 'get name'() {
        expect:
        property.name == 'my-prop'
    }

    def 'is synthetic'() {
        expect:
        property.synthetic
    }

    def 'get origin'() {
        expect:
        property.origin == origin
    }

    def 'get type'() {
        expect:
        property.type == BYTE
    }


}
