package io.github.joke.objects;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Filer;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;

import static java.util.Collections.singletonList;
import static javax.lang.model.SourceVersion.latestSupported;

@SupportedOptions({"debug"})
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends BasicAnnotationProcessor {

    @Override public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    protected Iterable<? extends Step> steps() {
        final Filer filer = processingEnv.getFiler();
        return singletonList(new BeanStep(filer));
    }

}
