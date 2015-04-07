package org.pcsoft.tools.scm_fx.plugin.scm_system.core;

import org.apache.commons.io.IOUtils;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.ScmSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.api.annotations.ScmSystemDescription;
import org.pcsoft.tools.scm_fx.plugin.scm_system.common.ScmSystemPluginLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by pfeifchr on 08.10.2014.
 */
public final class ScmSystemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScmSystemManager.class);

    private final Map<String, ScmSystemHolder> scmSystemMap = new LinkedHashMap<>();

    public ScmSystemManager(File pluginDirectory) {
        LOGGER.info("Load SCM System Manager...");
        try {
            LOGGER.debug("Load SCM System Plugins...");
            LOGGER.trace("> From: " + pluginDirectory.getAbsolutePath());
            final List<URL> urlList = new ArrayList<>();
            for (final File jarFile : pluginDirectory.listFiles(pathname -> pathname.getAbsolutePath().toLowerCase().endsWith(".jar"))) {
                LOGGER.trace(">>> Found JAR: " + jarFile.getName());
                urlList.add(jarFile.toURI().toURL());
            }
            final URLClassLoader urlClassLoader = new URLClassLoader(urlList.toArray(new URL[urlList.size()]),
                    getClass().getClassLoader());

            final ServiceLoader<ScmSystem> scmSystemServiceLoader = ServiceLoader.load(ScmSystem.class, urlClassLoader);
            final Iterator<ScmSystem> scmSystemIterator = scmSystemServiceLoader.iterator();

            final List<ScmSystemHolder> scmSystemList = new ArrayList<>();
            while (scmSystemIterator.hasNext()) {
                final ScmSystem scmSystem = scmSystemIterator.next();
                LOGGER.debug("Try to load SCM System: " + scmSystem.getClass().getName());
                final ScmSystemDescription scmSystemDescription = scmSystem.getClass().getAnnotation(ScmSystemDescription.class);
                if (scmSystemDescription == null)
                    throw new ScmSystemPluginLoadingException("Cannot load SCM system '" + scmSystem.getClass().getName() +
                            "': Needed annotation found: " + ScmSystemDescription.class.getName());

                LOGGER.info("> Load Plugin: " + scmSystemDescription.name() + " (" + scmSystemDescription.id() + ")");

                try {
                    final byte[] image;
                    if (scmSystemDescription.imageResourceName().isEmpty()) {
                        image = null;
                    } else {
                        //Use url class loader cause resources in it only (external plugged in JARs)
                        final InputStream stream = urlClassLoader.getResourceAsStream(
                                scmSystemDescription.imageResourceName()
                        );
                        if (stream == null) {
                            LOGGER.warn("Cannot load image for SCM system '" + scmSystem.getClass().getName() + "': " +
                                    scmSystemDescription.imageResourceName());
                            image = null;
                        } else {
                            image = IOUtils.toByteArray(stream);
                        }
                    }

                    scmSystemList.add(new ScmSystemHolder(
                            scmSystemDescription.id(), scmSystemDescription.name(), scmSystemDescription.description(),
                            scmSystemDescription.scmFile(), image, scmSystem
                    ));
                } catch (IOException e) {
                    LOGGER.error("Cannot find image resource for SCM system '" + scmSystem.getClass().getName() +
                            "': " + scmSystemDescription.imageResourceName(), e);
                    throw new ScmSystemPluginLoadingException("Cannot find image resource for SCM system '" + scmSystem.getClass().getName() +
                            "': " + scmSystemDescription.imageResourceName(), e);
                }
            }
            scmSystemList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

            scmSystemMap.clear();
            for (final ScmSystemHolder holder : scmSystemList) {
                scmSystemMap.put(holder.getId(), holder);
            }

            LOGGER.info("Finished loading SCM System Manager");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ScmSystemHolder> getScmSystemList() {
        return new ArrayList<>(scmSystemMap.values());
    }

    public Map<String, ScmSystemHolder> getScmSystemMap() {
        return scmSystemMap;
    }
}
