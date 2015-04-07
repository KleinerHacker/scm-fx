package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.utils;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.HgSystem;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgFileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.HgModification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.hg.types.xml.HgLog;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.simpleframework.xml.transform.Transform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;

/**
 * Created by pfeifchr on 15.10.2014.
 */
public final class HgXmlUtils {

    private static final class HgFileStateTransform implements Transform<HgFileState> {
        @Override
        public HgFileState read(String s) throws Exception {
            return HgFileState.fromKey(s.charAt(0));
        }

        @Override
        public String write(HgFileState HgFileState) throws Exception {
            return HgFileState.getKey() + "";
        }
    }

    private static final class HgModificationTransform implements Transform<HgModification> {
        @Override
        public HgModification read(String s) throws Exception {
            return HgModification.fromKey(s);
        }

        @Override
        public String write(HgModification HgModification) throws Exception {
            return HgModification.getKey();
        }
    }

    private static final class InstantTransform implements Transform<Instant> {
        @Override
        public Instant read(String s) throws Exception {
            if (HgUtils.hasTimeZone(s))
                return HgSystem.DATE_FORMAT_XML_TEXT.parse(s.substring(0, s.length()-6)).toInstant();

            return HgSystem.DATE_FORMAT_XML_TEXT.parse(s).toInstant();
        }

        @Override
        public String write(Instant instant) throws Exception {
            return instant.toString();
        }
    }

    private static final class URITransform implements Transform<URI> {
        @Override
        public URI read(String value) throws Exception {
            return new URI(value);
        }

        @Override
        public String write(URI value) throws Exception {
            return value.toString();
        }
    }

    public static HgLog readLogFromXml(File file) throws IOException {
        try {
            return buildPersister().read(HgLog.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static HgLog readLogFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(HgLog.class, in, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static Serializer buildPersister() {
        return new Persister(new RegistryMatcher() {
            @Override
            public Transform match(Class type) throws Exception {
                if (Instant.class.equals(type))
                    return new InstantTransform();
                else if (HgFileState.class.equals(type))
                    return new HgFileStateTransform();
                else if (HgModification.class.equals(type))
                    return new HgModificationTransform();
                else if (URI.class.equals(type))
                    return new URITransform();

                return super.match(type);
            }
        });
    }

    private HgXmlUtils() {
    }
}
