package org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation to use with {@link org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={TYPE})
public @interface ScmSystemDescription {

    /**
     * ID for this SCM system. Must be unique
     * @return
     */
    String id();

    /**
     * Name of SCM system
     * @return
     */
    String name();

    /**
     * Description for SCM System
     * @return
     */
    String description();

    /**
     * Optional image resource name for SCM System
     * @return
     */
    String imageResourceName() default "";

    /**
     * Defines the name of SCM file / directory for local cache store
     * @return
     */
    String scmFile();

}
