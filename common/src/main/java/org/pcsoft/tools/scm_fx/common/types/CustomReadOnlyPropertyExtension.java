package org.pcsoft.tools.scm_fx.common.types;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public interface CustomReadOnlyPropertyExtension<T> {

    ReadOnlyObjectProperty<T> getReadOnlyProperty();

}
