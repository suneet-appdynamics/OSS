/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006-2010 Oracle and/or its affiliates. All rights reserved.
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

/* CVS information
 * $Header: /cvs/glassfish/jmx-remote/rjmx-impl/src/java/com/sun/enterprise/admin/jmx/remote/server/callers/GetAttributeCaller.java,v 1.3 2005/12/25 04:26:38 tcfujii Exp $
 * $Revision: 1.3 $
 * $Date: 2005/12/25 04:26:38 $
*/

package com.sun.enterprise.admin.jmx.remote.server.callers;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.message.MBeanServerRequestMessage;
import javax.management.remote.message.MBeanServerResponseMessage;

/** Invokes the method getAttribute of the MBeanServerConnection.
 * @see MBeanServerRequestMessage#GET_ATTRIBUTE
 * @author Kedar Mhaswade
 * @since S1AS8.0
 * @version 1.0
 */
public class GetAttributeCaller extends AbstractMethodCaller {
    
    /** Creates a new instance of CreateMBeanCaller */
    public GetAttributeCaller(MBeanServerConnection mbsc) {
        super(mbsc);
        METHOD_ID = MBeanServerRequestMessage.GET_ATTRIBUTE;
    }
    
    public MBeanServerResponseMessage call(MBeanServerRequestMessage request) {
        Object result = null;
        boolean isException = false;
        try {
            result = mbsc.getAttribute((ObjectName)request.getParams()[0],
            (String)request.getParams()[1]);
        }
        catch(Throwable e) {
            result = e;
            isException = true;
        }
        return ( new MBeanServerResponseMessage(METHOD_ID, result, isException) );
    }
}
