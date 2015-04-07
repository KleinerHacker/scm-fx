package org.pcsoft.tools.scm_fx.common.types;

import org.ini4j.Profile;

/**
 * Created by pfeifchr on 09.10.2014.
 */
public interface ScmFxConfiguration {

    void save(Profile.Section section);
    void load(Profile.Section section);

}
