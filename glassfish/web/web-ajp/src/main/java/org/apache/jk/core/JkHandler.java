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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jk.core;

import java.io.IOException;
import java.util.Properties;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.tomcat.util.modeler.Registry;

/**
 *
 * @author Costin Manolache
 */
public class JkHandler implements MBeanRegistration, NotificationListener {
    public static final int OK=0;
    public static final int LAST=1;
    public static final int ERROR=2;

    protected Properties properties=new Properties();
    protected WorkerEnv wEnv;
    protected JkHandler next;
    protected String nextName=null;
    protected String name;
    protected int id;

    // XXX Will be replaced with notes and (configurable) ids
    // Each represents a 'chain' - similar with ActionCode in Coyote ( the concepts
    // will be merged ).    
    public static final int HANDLE_RECEIVE_PACKET   = 10;
    public static final int HANDLE_SEND_PACKET      = 11;
    public static final int HANDLE_FLUSH            = 12;
    public static final int HANDLE_THREAD_END       = 13;
    
    public void setWorkerEnv( WorkerEnv we ) {
        this.wEnv=we;
    }

    /** Set the name of the handler. Will allways be called by
     *  worker env after creating the worker.
     */
    public void setName(String s ) {
        name=s;
    }

    public String getName() {
        return name;
    }

    /** Set the id of the worker. We use an id for faster dispatch.
     *  Since we expect a decent number of handler in system, the
     *  id is unique - that means we may have to allocate bigger
     *  dispatch tables. ( easy to fix if needed )
     */
    public void setId( int id ) {
        this.id=id;
    }

    public int getId() {
        return id;
    }
    
    /** Catalina-style "recursive" invocation.
     *  A chain is used for Apache/3.3 style iterative invocation.
     */
    public void setNext( JkHandler h ) {
        next=h;
    }

    public void setNext( String s ) {
        nextName=s;
    }

    public String getNext() {
        if( nextName==null ) {
            if( next!=null)
                nextName=next.getName();
        }
        return nextName;
    }

    /** Should register the request types it can handle,
     *   same style as apache2.
     */
    public void init() throws IOException {
    }

    /** Clean up and stop the handler
     */
    public void destroy() throws IOException {
    }

     public MsgContext createMsgContext() {
         return new MsgContext(8*1024);
     }

     public MsgContext createMsgContext(int bsize) {
        return new MsgContext(bsize);
     }
 
    public int invoke(Msg msg, MsgContext mc )  throws IOException {
        return OK;
    }
    
    public void setProperty( String name, String value ) {
        properties.put( name, value );
    }

    public String getProperty( String name ) {
        return properties.getProperty(name) ;
    }

    /** Experimental, will be replaced. This allows handlers to be
     *  notified when other handlers are added.
     */
    public void addHandlerCallback( JkHandler w ) {

    }

    public void handleNotification(Notification notification, Object handback)
    {
//        BaseNotification bNot=(BaseNotification)notification;
//        int code=bNot.getCode();
//
//        MsgContext ctx=(MsgContext)bNot.getSource();


    }

    protected String domain;
    protected ObjectName oname;
    protected MBeanServer mserver;

    public ObjectName getObjectName() {
        return oname;
    }

    public String getDomain() {
        return domain;
    }

    public ObjectName preRegister(MBeanServer server,
                                  ObjectName oname) throws Exception {
        this.oname=oname;
        mserver=server;
        domain=oname.getDomain();
        if( name==null ) {
            name=oname.getKeyProperty("name");
        }
        
        // we need to create a workerEnv or set one.
        ObjectName wEnvName=new ObjectName(domain + ":type=JkWorkerEnv");
        if ( wEnv == null ) {
            wEnv=new WorkerEnv();
        }
        if( ! mserver.isRegistered(wEnvName )) {
            Registry.getRegistry(null, null).registerComponent(wEnv, wEnvName, null);
        }
        mserver.invoke( wEnvName, "addHandler", 
                new Object[] {name, this}, 
                new String[] {"java.lang.String", 
                              "org.apache.jk.core.JkHandler"});
        return oname;
    }
    
    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
    }

    public void pause() throws Exception {
    }

    public void resume() throws Exception {
    }

}
