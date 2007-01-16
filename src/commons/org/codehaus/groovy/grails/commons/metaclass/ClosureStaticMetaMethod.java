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
package org.codehaus.groovy.grails.commons.metaclass;

import java.lang.reflect.Modifier;

import groovy.lang.Closure;

import org.codehaus.groovy.runtime.NewStaticMetaMethod;

/**
 * This class represents a MetaMethod that is a closure that pretends to be a static method.
 * It is used by ExpandoMetaClass to allow addition of static methods defined as closures
 * 
 * @author Graeme Rocher
 * @since 0.4
 */
public class ClosureStaticMetaMethod extends NewStaticMetaMethod implements ClosureInvokingMethod {

	private Closure callable;
	private Class[] paramTypes;
	private Class declaringClass;

	public ClosureStaticMetaMethod(String name, Class declaringClass, Closure c) {
		super(name, declaringClass, c.getParameterTypes(), Object.class, Modifier.PUBLIC);
		paramTypes = c.getParameterTypes();
		if(paramTypes == null) {
			paramTypes = new Class[0];
		}
		this.callable = c;
		this.declaringClass = declaringClass;

	}

	/* (non-Javadoc)
	 * @see groovy.lang.MetaMethod#invoke(java.lang.Object, java.lang.Object[])
	 */
	public Object invoke(Object object, Object[] arguments) {
		Closure cloned = (Closure) callable.clone();
		cloned.setDelegate(object);
		return cloned.call(arguments);
	}

	/* (non-Javadoc)
	 * @see org.codehaus.groovy.runtime.NewInstanceMetaMethod#getParameterTypes()
	 */
	public Class[] getParameterTypes() {
		return paramTypes;
	}

	/* (non-Javadoc)
	 * @see org.codehaus.groovy.runtime.NewStaticMetaMethod#getDeclaringClass()
	 */
	public Class getDeclaringClass() {
		return this.declaringClass;
	}

	public Closure getClosure() {
		return this.callable;
	}
	
	
	
}
