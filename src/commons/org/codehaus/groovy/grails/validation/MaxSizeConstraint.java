/* Copyright 2004-2005 Graeme Rocher
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
package org.codehaus.groovy.grails.validation;

import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.springframework.validation.Errors;

import grails.util.GrailsUtil;

import java.util.Collection;
import java.lang.reflect.Array;

/**
 *
 * A constraint that validates maximum size of the property, for strings and arrays this is the length, collections
 * the size and numbers the value

 * @author Graeme Rocher
 * @since 0.4
 *        <p/>
 *        Created: Jan 19, 2007
 *        Time: 8:29:02 AM
 */
class MaxSizeConstraint extends AbstractConstraint {

	private int maxSize;

    /**
     * @return Returns the maxSize.
     */
    public int getMaxSize() {
        return maxSize;
    }
    /* (non-Javadoc)
     * @see org.codehaus.groovy.grails.validation.ConstrainedProperty.AbstractConstraint#setParameter(java.lang.Object)
     */
    public void setParameter(Object constraintParameter) {
        if(!(constraintParameter instanceof Integer))
            throw new IllegalArgumentException("Parameter for constraint ["+ConstrainedProperty.MAX_SIZE_CONSTRAINT+"] of property ["+constraintPropertyName+"] of class ["+constraintOwningClass+"] must be a of type [java.lang.Integer]");

        this.maxSize = ((Integer)constraintParameter).intValue();
        super.setParameter(constraintParameter);
    }

    public String getName() {
        return ConstrainedProperty.MAX_SIZE_CONSTRAINT;
    }

    /* (non-Javadoc)
       * @see org.codehaus.groovy.grails.validation.Constraint#supports(java.lang.Class)
       */
    public boolean supports(Class type) {
    	if(GrailsClassUtils.isAssignableOrConvertibleFrom(Number.class, type)) {
    		GrailsUtil.deprecated("'maxSize' constraint is deprecated for numeric properties and will be removed in 0.6, use 'max' constraint instead");
    	}
        return type != null && (Comparable.class.isAssignableFrom(type) ||
        		GrailsClassUtils.isAssignableOrConvertibleFrom(Number.class, type) ||
                Collection.class.isAssignableFrom(type) ||
                type.isArray());

    }

    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        Object[] args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue, new Integer(maxSize) };

        if(propertyValue == null) {
            return; // A null is not a value we should even check
        }

        else if(propertyValue.getClass().isArray()) {
            int length = Array.getLength( propertyValue );
            if(length > maxSize) {
                super.rejectValue(errors,ConstrainedProperty.MAX_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX,args,getDefaultMessage(ConstrainedProperty.DEFAULT_INVALID_MAX_SIZE_MESSAGE_CODE, args));
            }
        }
        else if(propertyValue instanceof Collection) {
            if (((Collection) propertyValue).size() > maxSize) {
                super.rejectValue(errors, ConstrainedProperty.MAX_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX, args, getDefaultMessage(ConstrainedProperty.DEFAULT_INVALID_MAX_SIZE_MESSAGE_CODE, args));
            }
        }
        else if (propertyValue instanceof Number) {
            int numberSize = ((Number) propertyValue).intValue();
            if (numberSize > maxSize) {
                super.rejectValue(errors, ConstrainedProperty.MAX_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX, args, getDefaultMessage(ConstrainedProperty.DEFAULT_INVALID_MAX_SIZE_MESSAGE_CODE, args));
            }
        }
        else if (propertyValue instanceof String) {
            if (((String) propertyValue).length() > maxSize) {
                super.rejectValue(errors, ConstrainedProperty.MAX_SIZE_CONSTRAINT + ConstrainedProperty.EXCEEDED_SUFFIX, args, getDefaultMessage(ConstrainedProperty.DEFAULT_INVALID_MAX_SIZE_MESSAGE_CODE, args));
            }
        }
    }
}
