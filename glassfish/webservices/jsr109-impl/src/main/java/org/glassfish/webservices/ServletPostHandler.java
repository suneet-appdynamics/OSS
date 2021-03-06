/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.webservices;

import java.lang.reflect.Method;

import java.rmi.UnmarshalException;

import javax.xml.namespace.QName;

import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;

/*import com.sun.enterprise.Switch;
import com.sun.enterprise.ComponentInvocation;
import com.sun.enterprise.InvocationManager;*/

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ResourceBundle;
import java.text.MessageFormat;

import com.sun.logging.LogDomains;
import com.sun.enterprise.web.WebComponentInvocation;
import org.glassfish.api.invocation.InvocationManager;
import org.glassfish.ejb.api.EJBInvocation;

/**
 * This handler is inserted last in the handler chain for an
 * servlet web service endpoint.  
 *
 * @author Kenneth Saks
 */
public class ServletPostHandler extends GenericHandler {

    private static Logger logger = 
        LogDomains.getLogger(ServletPostHandler.class, LogDomains.WEBSERVICES_LOGGER);
    private WsUtil wsUtil = new WsUtil();
    private ResourceBundle rb = logger.getResourceBundle();


    public ServletPostHandler() {}

    public QName[] getHeaders() {
        return new QName[0];
    }

    public boolean handleRequest(MessageContext context) {
        WebComponentInvocation inv = null;

        try {
            WebServiceContractImpl wscImpl = WebServiceContractImpl.getInstance();
            InvocationManager invManager = wscImpl.getInvocationManager();
            inv = (WebComponentInvocation) invManager.getCurrentInvocation();
            Method webServiceMethodInPreHandler = inv.getWebServiceMethod();
            if( webServiceMethodInPreHandler != null ) {
                // Now that application handlers have run, do another method
                // lookup and compare the results with the original one.  This
                // ensures that the application handlers have not changed
                // the message context in any way that would impact which
                // method is invoked.
                Method postHandlerMethod = wsUtil.getInvMethod(
                        (com.sun.xml.rpc.spi.runtime.Tie)inv.getWebServiceTie(), context);
                if( !webServiceMethodInPreHandler.equals(postHandlerMethod) ) {
                    throw new UnmarshalException("Original method " + webServiceMethodInPreHandler +
                            " does not match post-handler method ");
                }
            }
        } catch(Exception e) {
            logger.log(Level.WARNING, format(rb.getString("postWebHandlerError"), e.toString()));
            wsUtil.throwSOAPFaultException(e.getMessage(), context);
        }
        return true;
    }


    private String format(String key, String ... values){
        return MessageFormat.format(key, (Object [])values);
    }
}
