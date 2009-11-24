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

package org.codehaus.groovy.grails.web.taglib

import org.codehaus.groovy.grails.web.util.TypeConvertingMap

/**
 * Class used to define attributes passed to a GSP tag. Mixes in
 * TypeConvertingMap for ease of type conversion
 *
 * @author Graeme Rocher
 * @since 1.2
 */

@Mixin(org.codehaus.groovy.grails.web.util.TypeConvertingMap)
public class GroovyPageAttributes extends LinkedHashMap{

    GroovyPageAttributes() {
        getMetaClass().mixin TypeConvertingMap
    }

    GroovyPageAttributes(Map map) {
        super(map);
        getMetaClass().mixin TypeConvertingMap
    }
}