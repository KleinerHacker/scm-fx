package org.pcsoft.tools.scm_fx.plugin.scm_system.api.resolvers;

/**
 * Resolver to resolve revision numbers
 */
public interface RevisionNumberResolver extends Resolver {
    long getCurrentRevision();
}