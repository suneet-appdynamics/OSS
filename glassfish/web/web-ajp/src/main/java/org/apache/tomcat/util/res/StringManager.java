/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
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

package org.apache.tomcat.util.res;

import java.text.MessageFormat;
import java.util.*;

/**
 * An internationalization / localization helper class which reduces
 * the bother of handling ResourceBundles and takes care of the
 * common cases of message formating which otherwise require the
 * creation of Object arrays and such.
 *
 * <p>The StringManager operates on a package basis. One StringManager
 * per package can be created and accessed via the getManager method
 * call.
 *
 * <p>The StringManager will look for a ResourceBundle named by
 * the package name given plus the suffix of "LocalStrings". In
 * practice, this means that the localized information will be contained
 * in a LocalStrings.properties file located in the package
 * directory of the classpath.
 *
 * <p>Please see the documentation for java.util.ResourceBundle for
 * more information.
 *
 * @version $Revision: 1.3 $ $Date: 2007/05/05 05:33:17 $
 *
 * @author James Duncan Davidson [duncan@eng.sun.com]
 * @author James Todd [gonzo@eng.sun.com]
 * @author Mel Martinez [mmartinez@g1440.com]
 * @see java.util.ResourceBundle
 */

public class StringManager {

    /**
     * The ResourceBundle for this StringManager.
     */

    private volatile ResourceBundle bundle;

    private String packageName;
    private Locale locale;
    private ClassLoader classLoader;

    /**
     * Creates a new StringManager for a given package. This is a
     * private method and all access to it is arbitrated by the
     * static getManager method call so that only one StringManager
     * per package will be created.
     *
     * @param packageName Name of package to create StringManager for.
     */

    private StringManager(String packageName) {
	this( packageName, Locale.getDefault() );
    }

    private StringManager(String packageName,Locale loc) {
        this.packageName = packageName;
        this.locale = loc;
        this.classLoader = Thread.currentThread().getContextClassLoader();
    }

    private ResourceBundle getResourceBundle() {
        if(bundle!=null)    return bundle;

        String bundleName = packageName + ".LocalStrings";
        try {
            bundle = ResourceBundle.getBundle(bundleName,locale,classLoader);
        } catch( MissingResourceException ex ) {
            bundle= ResourceBundle.getBundle( bundleName,Locale.US,classLoader);
        }
        return bundle;
    }

    private StringManager(ResourceBundle bundle )
    {
	this.bundle=bundle;
    }

    /**
        Get a string from the underlying resource bundle or return
        null if the String is not found.
     
        @param key to desired resource String
        @return resource String matching <i>key</i> from underlying
                bundle or null if not found.
        @throws IllegalArgumentException if <i>key</i> is null.        
     */

    public String getString(String key) {
        if(key == null){
            String msg = "key may not have a null value";

            throw new IllegalArgumentException(msg);
        }

        String str = null;

        try{
            str = getResourceBundle().getString(key);
        }catch(MissingResourceException mre){
            //bad: shouldn't mask an exception the following way:
            //   str = "[cannot find message associated with key '" + key + "' due to " + mre + "]";
	        //     because it hides the fact that the String was missing
	        //     from the calling code.
	        //good: could just throw the exception (or wrap it in another)
	        //      but that would probably cause much havoc on existing
	        //      code.
	        //better: consistent with container pattern to
	        //      simply return null.  Calling code can then do
	        //      a null check.
	        str = null;
        }

        return str;
    }

    /**
     * Get a string from the underlying resource bundle and format
     * it with the given set of arguments.
     *
     * @param key
     * @param args
     */

    public String getString(String key, Object[] args) {
        String iString = null;
        String value = getString(key);

        // this check for the runtime exception is some pre 1.1.6
        // VM's don't do an automatic toString() on the passed in
        // objects and barf out

        try {
            // ensure the arguments are not null so pre 1.2 VM's don't barf
            if(args==null){
                args = new Object[1];
            }
            
            Object[] nonNullArgs = args;
            for (int i=0; i<args.length; i++) {
                if (args[i] == null) {
                    if (nonNullArgs==args){
                        nonNullArgs=(Object[])args.clone();
                    }
                    nonNullArgs[i] = "null";
                }
            }
            if( value==null ) value=key;
            iString = MessageFormat.format(value, nonNullArgs);
        } catch (IllegalArgumentException iae) {
            StringBuilder buf = new StringBuilder();
            buf.append(value);
            for (int i = 0; i < args.length; i++) {
                buf.append(" arg[" + i + "]=" + args[i]);
            }
            iString = buf.toString();
        }
        return iString;
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object argument. This argument can of course be
     * a String object.
     *
     * @param key
     * @param arg
     */

    public String getString(String key, Object arg) {
	Object[] args = new Object[] {arg};
	return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     */

    public String getString(String key, Object arg1, Object arg2) {
	Object[] args = new Object[] {arg1, arg2};
	return getString(key, args);
    }
    
    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     */

    public String getString(String key, Object arg1, Object arg2,
			    Object arg3) {
	Object[] args = new Object[] {arg1, arg2, arg3};
	return getString(key, args);
    }

    /**
     * Get a string from the underlying resource bundle and format it
     * with the given object arguments. These arguments can of course
     * be String objects.
     *
     * @param key
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */

    public String getString(String key, Object arg1, Object arg2,
			    Object arg3, Object arg4) {
	Object[] args = new Object[] {arg1, arg2, arg3, arg4};
	return getString(key, args);
    }
    // --------------------------------------------------------------
    // STATIC SUPPORT METHODS
    // --------------------------------------------------------------

    private static Hashtable<String, StringManager> managers =
            new Hashtable<String, StringManager>();

    /**
     * Get the StringManager for a particular package. If a manager for
     * a package already exists, it will be reused, else a new
     * StringManager will be created and returned.
     *
     * @param packageName
     */

    public synchronized static StringManager getManager(String packageName) {
      StringManager mgr = (StringManager)managers.get(packageName);
      if (mgr == null) {
          mgr = new StringManager(packageName);
          managers.put(packageName, mgr);
      }
      return mgr;
    }

    /**
     * Get the StringManager for a particular package. If a manager for
     * a package already exists, it will be reused, else a new
     * StringManager will be created and returned.
     */
    public synchronized static StringManager getManager(ResourceBundle bundle) {
      return new StringManager( bundle );
    }

    /**
     * Get the StringManager for a particular package and Locale. If a manager for
     * a package already exists, it will be reused, else a new
     * StringManager will be created for that Locale and returned.
     *
     *
     * @param packageName
     */

   public synchronized static StringManager getManager(String packageName,Locale loc) {
      StringManager mgr = (StringManager)managers.get(packageName+"_"+loc.toString());
      if (mgr == null) {
          mgr = new StringManager(packageName,loc);
          managers.put(packageName+"_"+loc.toString(), mgr);
      }
      return mgr;
    }

}
