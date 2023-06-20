package com.morpheusdata.core;

/**
 * Provides an interface for defining custom external Credential Stores. An example might be to store secure passwords/credentials for interfacing with other integrations or clouds using
 * a third party secrets management system such as CyberArk or HashiCorp Vault
 *
 * NOTE: This provider has been moved and is deprecated. {@link com.morpheusdata.core.providers.CredentialProvider}
 *
 * @since 0.13.1
 * @see com.morpheusdata.core.providers.CredentialProvider
 * @deprecated
 * @author David Estes
 */
@Deprecated
public interface CredentialProvider extends com.morpheusdata.core.providers.CredentialProvider {
}
