package com.surevine.chat.openfire.auth;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.AuthProvider;
import org.jivesoftware.openfire.auth.ConnectionException;
import org.jivesoftware.openfire.auth.InternalUnauthenticatedException;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveProperties;
import com.surevine.chat.openfire.audit.AuditPlugin;

/**
 *
 * This class provides for a "magic password" which will allow someone who knows
 * it to authenticate as any user.  For obvious reasons, the password should be
 * set and managed very securely.
 * 
 * This class will audit all failed attempts to authenticate using the audit
 * mechanism.  Optionally, it can also audit succesful attempts to login
 * 
 * To use this class, place it on openfire's classpath (in /opt/openfire/lib
 * is fine) and set the following properties in the openfire
 * "system properties" page:
 * 
 *   hybridAuthProvider.tertiaryProvider.className = com.surevine.chat.openfire.auth.SuperuserAuthProvider
 *   com.surevine.chat.openfire.auth.superuserPassword = [the password]
 *   (optionally)  com.surevine.chat.openfire.auth.logSuperuserLogins = true 
 *   (optionally)  com.surevine.chat.openfire.auth.auditPluginName = [the name of the audit plugin to use]
 *   
 */
public class SuperuserAuthProvider implements AuthProvider {

	private static final String PASSWORD_PROPERTY="com.surevine.chat.openfire.auth.superuserPassword";
	private static final String AUDIT_PLUGIN_PROPERTY_NAME="com.surevine.chat.openfire.auth.auditPluginName";
	private static AuditPlugin auditPlugin = null;

    public boolean hasWarnedNoAuditing = false;
	
	public void authenticate(String userName, String password) throws UnauthorizedException, ConnectionException, InternalUnauthenticatedException {
		if (auditPlugin == null) {
			auditPlugin = (AuditPlugin)(XMPPServer.getInstance().getPluginManager().getPlugin(JiveProperties.getInstance().get(AUDIT_PLUGIN_PROPERTY_NAME)));
		}

        boolean doAudit = false;
        if (auditPlugin == null && !hasWarnedNoAuditing) {
            System.out.println("SuperuserAuthProvider.authenticate() Warning: audit plugin not found.");

            hasWarnedNoAuditing = true;
        } else {
            doAudit = JiveProperties.getInstance().getBooleanProperty("com.surevine.chat.openfire.auth.logSuperuserLogins");
        }

        if (!password.equals(JiveProperties.getInstance().get(PASSWORD_PROPERTY))) {
    		if (doAudit) {
                auditPlugin.getAuditMessageFactory().createAuditMessageForSULogin(userName, false);
            }

    		throw new UnauthorizedException("The super user provided an incorrect password");
    	}

        if (doAudit) {
    		auditPlugin.getAuditMessageFactory().createAuditMessageForSULogin(userName, true);
        }
	}

	public void authenticate(String arg0, String arg1, String arg2) throws UnauthorizedException, ConnectionException, InternalUnauthenticatedException {
		throw new UnsupportedOperationException("Method not supported");
	}

	public String getPassword(String arg0) throws UserNotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Method not supported");
	}

	public boolean isDigestSupported() {
		return false;
	}

	public boolean isPlainSupported() {
		return true;
	}

	public void setPassword(String arg0, String arg1) throws UserNotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException("Method not supported");
	}

	public boolean supportsPasswordRetrieval() {
		return false;
	}

}
