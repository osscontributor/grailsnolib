/*
 * Copyright 2004-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.codehaus.groovy.grails.commons;

import groovy.lang.GroovyClassLoader;

/**
 *  <p>Exposes all classes for a Grails application.
 * 
 * @author Steven Devijver
 * @since Jul 2, 2005
 */
public interface GrailsApplication {

	
	/**
	 * <p>Returns all controllers in an application
	 * 
	 * @return controllers in an application
	 */
	public GrailsControllerClass[] getControllers();
	
	/**
	 * <p>Returns the controller with the given full name or null if no controller was found with that name.
	 * 
	 * @param name the controller full name
	 * @return the controller or null if no controller was found.
	 */
	public GrailsControllerClass getController(String fullname);
	
	/**
	 * <p>Returns the controllers that maps to the given URI or null if no controller was found with that name.
	 * 
	 * @param uri the uri of the request
	 * @return the controller or null if no controller was found
	 */
	public GrailsControllerClass getControllerByURI(String uri);
	
	/**
	 * <p>Returns the class loader instance for the Grails application</p>
	 * 
	 * @return The GroovyClassLoader instance
	 */
	public GroovyClassLoader getClassLoader();
}
