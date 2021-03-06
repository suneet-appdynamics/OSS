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

package com.sun.enterprise.tools.verifier.tests.ejb.ejb30;

import com.sun.enterprise.tools.verifier.tests.ejb.EjbTest;
import com.sun.enterprise.tools.verifier.tests.ComponentNameConstructor;
import com.sun.enterprise.tools.verifier.Result;
import com.sun.enterprise.tools.verifier.Verifier;
import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.EjbInterceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Interceptor methods may throw runtime exceptions or application exceptions 
 * that are allowed in the throws clause of the business method.
 * The methods of the business interface should not throw the 
 * java.rmi.RemoteException, even if the interface is a remote business 
 * interface or the bean class is annotated WebService or the method as 
 * WebMethod.
 * 
 * These statements from the specification indicate that the interceptor method
 * must not throw java.rmi.RemoteException
 * 
 * @author Vikas Awasthi
 */
public class InterceptorMethodException extends EjbTest {

    public Result check(EjbDescriptor descriptor) {
        Result result = getInitializedResult();
        ComponentNameConstructor compName = getVerifierContext().getComponentNameConstructor();
        Set<Method> interceptorMethods = new HashSet<Method>();

        if(descriptor.hasAroundInvokeMethod()) {
//XXX
/*
            Method method = descriptor.getAroundInvokeMethod().getMethod(descriptor);
            interceptorMethods.add(method);
*/
        }
        
        List<EjbInterceptor> interceptors = descriptor.getInterceptorChain();
        
        for (EjbInterceptor interceptor : interceptors) {
            try {
                Class interceptorClass = 
                        Class.forName(interceptor.getInterceptorClassName(),
                                      false,
                                      getVerifierContext().getClassLoader());
//XXX
/*
                Method method = interceptor.getAroundInvokeMethod().getMethod(interceptorClass);
                interceptorMethods.add(method);
*/
            } catch (ClassNotFoundException e) {
                Verifier.debug(e);
                addErrorDetails(result, compName);
                result.failed(smh.getLocalString
                                (getClass().getName() + ".failed1",
                                "[ {0} ] not found.",
                                new Object[] {interceptor.getInterceptorClassName()}));
            }
        }
        
        for (Method method : interceptorMethods) {
            Class[] exceptions = method.getExceptionTypes();
            for (Class excepClass : exceptions) {
                if(java.rmi.RemoteException.class.isAssignableFrom(excepClass)) {
                    addErrorDetails(result, compName);
                    result.failed(smh.getLocalString
                                    (getClass().getName() + ".failed",
                                    "Method [ {0} ] throws java.rmi.RemoteException.",
                                    new Object[] {method}));
                }
            }
        }
        
        if(result.getStatus()!=Result.FAILED) {
            addGoodDetails(result, compName);
            result.passed(smh.getLocalString
                            (getClass().getName() + ".passed",
                            "Valid Interceptor methods."));
        }

        return result;
    }
}
