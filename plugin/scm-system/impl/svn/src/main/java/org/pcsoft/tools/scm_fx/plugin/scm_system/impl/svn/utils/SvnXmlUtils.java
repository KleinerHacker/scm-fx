package org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.utils;

import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnFileState;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnKindOfFile;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.SvnModification;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnInfo;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnLog;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnPropertyList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnRepoList;
import org.pcsoft.tools.scm_fx.plugin.scm_system.impl.svn.types.xml.SvnStatus;
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
 * Created by pfeifchr on 09.10.2014.
 */
public final class SvnXmlUtils {

    private static final class SvnFileStateTransform implements Transform<SvnFileState> {
        @Override
        public SvnFileState read(String s) throws Exception {
            return SvnFileState.fromKey(s);
        }

        @Override
        public String write(SvnFileState svnFileState) throws Exception {
            return svnFileState.getKey();
        }
    }

    private static final class SvnModificationTransform implements Transform<SvnModification> {
        @Override
        public SvnModification read(String s) throws Exception {
            return SvnModification.fromKey(s);
        }

        @Override
        public String write(SvnModification svnModification) throws Exception {
            return svnModification.getKey();
        }
    }

    private static final class SvnKindOfFileTransform implements Transform<SvnKindOfFile> {
        @Override
        public SvnKindOfFile read(String s) throws Exception {
            return SvnKindOfFile.fromId(s);
        }

        @Override
        public String write(SvnKindOfFile svnKindOfFile) throws Exception {
            return svnKindOfFile.getId();
        }
    }

    private static final class InstantTransform implements Transform<Instant> {
        @Override
        public Instant read(String s) throws Exception {
            return Instant.parse(s);
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

    public static SvnRepoList readSvnRepoListFromXml(File file) throws IOException {
        try {
            return buildPersister().read(SvnRepoList.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnRepoList readSvnRepoListFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(SvnRepoList.class, in, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnLog readSvnLogFromXml(File file) throws IOException {
        try {
            return buildPersister().read(SvnLog.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnLog readSvnLogFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(SvnLog.class, in, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnStatus readSvnStatusFromXml(File file) throws IOException {
        try {
            return buildPersister().read(SvnStatus.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnStatus readSvnStatusFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(SvnStatus.class, in, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnPropertyList readSvnPropertiesFromXml(File file) throws IOException {
        try {
            return buildPersister().read(SvnPropertyList.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnPropertyList readSvnPropertiesFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(SvnPropertyList.class, in, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnInfo readSvnInfoFromXml(File file) throws IOException {
        try {
            return buildPersister().read(SvnInfo.class, file, false);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static SvnInfo readSvnInfoFromXml(InputStream in) throws IOException {
        try {
            return buildPersister().read(SvnInfo.class, in, false);
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
                else if (SvnFileState.class.equals(type))
                    return new SvnFileStateTransform();
                else if (SvnKindOfFile.class.equals(type))
                    return new SvnKindOfFileTransform();
                else if (SvnModification.class.equals(type))
                    return new SvnModificationTransform();
                else if (URI.class.equals(type))
                    return new URITransform();

                return super.match(type);
            }
        });
    }

}
