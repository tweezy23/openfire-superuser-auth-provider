Openfire Superuser Auth Provider
================================

This is a very simple AuthProvider which provides for a "magic password" which will
allow someone who knows it to authenticate as any user.  For obvious reasons, the password
should be set and managed very securely.

This will audit all failed attempts to authenticate using the openfire-audit-plugin
mechanism (if available).  Optionally, it can also audit succesful attempts to login.

To use this class, place it on openfire's classpath (in /opt/openfire/lib is fine) and set
the following properties in the openfire "system properties" page:

> * hybridAuthProvider.tertiaryProvider.className = com.surevine.chat.openfire.auth.SuperuserAuthProvider
> * com.surevine.chat.openfire.auth.superuserPassword = [the password]
> * (optionally)  com.surevine.chat.openfire.auth.logSuperuserLogins = true 
> * (optionally)  com.surevine.chat.openfire.auth.auditPluginName = [the name of the audit plugin to use]

DO NOT USE THIS UNLESS YOU KNOW WHAT YOU ARE DOING!
---------------------------------------------------

This provider essentially opens a security hole in your Openfire installation by
prioviding a single "skeleton key" password which will let anyone with knowledge of that
password log in and masqerade as anyone else, include any administrative user. It was
built for a very specific purpose, and probably has little value outside of that intended
purpose.

You have been warned.