/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.admin.amx.intf.config.grizzly;

import org.glassfish.admin.amx.base.Singleton;
import org.glassfish.admin.amx.intf.config.ConfigElement;
import org.glassfish.admin.amx.intf.config.PropertiesAccess;

/**
 * Note: attribute getters/setters are not included in this interface; use generic approach.
 */
@Deprecated
public interface Http extends ConfigElement, Singleton, PropertiesAccess {
    public FileCache getFileCache();

    public String getRestrictedUserAgents();

    public void setRestrictedUserAgents(String val);

    public String getRcmSupportEnabled();

    public void setRcmSupportEnabled(String val);

    public String getRedirectPort();

    public void setRedirectPort(String val);

    public String getMaxPostSizeBytes();

    public void setMaxPostSizeBytes(String val);

    public String getDefaultResponseType();

    public void setDefaultResponseType(String val);

    public String getSendBufferSizeBytes();

    public void setSendBufferSizeBytes(String val);

    public String getServerName();

    public void setServerName(String val);

    public String getConnectionUploadTimeoutMillis();

    public void setConnectionUploadTimeoutMillis(String val);


    public String getCompressableMimeType();

    public void setCompressableMimeType(String val);

    public String getXpoweredBy();

    public void setXpoweredBy(String val);

    public String getCometSupportEnabled();

    public void setCometSupportEnabled(String val);

    public String getMaxConnections();

    public void setMaxConnections(String val);

    public String getCompressionMinSizeBytes();

    public void setCompressionMinSizeBytes(String val);

    public String getUriEncoding();

    public void setUriEncoding(String val);

    public String getForcedResponseType();

    public void setForcedResponseType(String val);

    public String getVersion();

    public void setVersion(String val);

    public String getUploadTimeoutEnabled();

    public void setUploadTimeoutEnabled(String val);

    public String getNoCompressionUserAgents();

    public void setNoCompressionUserAgents(String val);

    public String getTimeoutSeconds();

    public void setTimeoutSeconds(String val);

    public String getDefaultVirtualServer();

    public void setDefaultVirtualServer(String val);

    public String getCompression();

    public void setCompression(String val);

    public String getRequestTimeoutSeconds();

    public void setRequestTimeoutSeconds(String val);

    public String getChunkingEnabled();

    public void setChunkingEnabled(String val);

    public String getAuthPassThroughEnabled();

    public void setAuthPassThroughEnabled(String val);

    public String getDnsLookupEnabled();

    public void setDnsLookupEnabled(String val);

    public String getHeaderBufferLengthBytes();

    public void setHeaderBufferLengthBytes(String val);

    public String getAdapter();

    public void setAdapter(String val);

    public String getTraceEnabled();

    public void setTraceEnabled(String val);

}



