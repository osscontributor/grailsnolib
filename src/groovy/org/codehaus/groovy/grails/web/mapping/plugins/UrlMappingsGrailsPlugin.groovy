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
package org.codehaus.groovy.grails.web.mapping.plugins;

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.web.mapping.*
import org.codehaus.groovy.grails.commons.*
/**
 * A plug-in that handles the configuration of URL mappings for Grails
 *
 * @author Graeme Rocher
 * @since 0.4
 */
class UrlMappingsGrailsPlugin {

	def watchedResources = ["file:./grails-app/conf/*UrlMappings.groovy"]

	def version = GrailsUtil.getGrailsVersion()
	def dependsOn = [controllers:version]

	def doWithSpring = {
        grailsUrlMappingsHolder(UrlMappingsHolderFactoryBean) {
            grailsApplication = ref("grailsApplication", true)
        }
	}


	def onChange = { event ->
	    if(application.isUrlMappingsClass(event.source)) {
	        application.addArtefact( UrlMappingsArtefactHandler.TYPE, event.source )
	        
            def beans = beans {
                grailsUrlMappingsHolder(UrlMappingsHolderFactoryBean) {
                    grailsApplication = ref("grailsApplication", true)
                }
            }
            event.ctx.registerBeanDefinition(UrlMappingsHolder.BEAN_ID, beans.getBeanDefinition(UrlMappingsHolder.BEAN_ID))
        }
	}
}