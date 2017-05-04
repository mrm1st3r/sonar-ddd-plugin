package de.smartsquare.ddd.sonarqube.collect;

import com.google.common.collect.ImmutableList;
import de.smartsquare.ddd.annotations.DDDEntity;
import org.sonar.api.config.Settings;

import java.util.Collections;
import java.util.List;

/**
 * Collector for entity classes.
 */
public class EntityCollector extends ModelCollector {

    private final Settings settings;

    public EntityCollector(Settings settings, ModelCollectionBuilder builder) {
        super(builder);
        this.settings = settings;
    }

    @Override
    ModelCollection.Type getModelType() {
        return ModelCollection.Type.ENTITY;
    }

    @Override
    List<String> getAnnotations() {
        return ImmutableList.of(DDDEntity.class.getName());
    }

    @Override
    List<String> getSuperClasses() {
        return Collections.emptyList();
    }

    @Override
    String getNamePattern() {
        return null;
    }
}
