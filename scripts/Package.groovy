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

/**
 * Gant script that packages a Grails application (note: does not create WAR)
 * 
 * @author Graeme Rocher
 *
 * @since 0.4
 */

import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import groovy.text.SimpleTemplateEngine
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.*
import org.codehaus.groovy.grails.plugins.*
import org.codehaus.groovy.grails.commons.*
import org.codehaus.groovy.control.*    
import grails.util.*


Ant.property(environment:"env")                             
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"    

includeTargets << new File ( "${grailsHome}/scripts/Init.groovy" )  
includeTargets << new File ( "${grailsHome}/scripts/Compile.groovy" )  
includeTargets << new File ( "${grailsHome}/scripts/PackagePlugins.groovy" ) 

scaffoldDir = "${basedir}/web-app/WEB-INF/templates/scaffolding"     
config = new ConfigObject()

task ('default': "Packages a Grails application. Note: To create WAR use 'grails war'") {
     depends( checkVersion)
	 packagePlugins()	 
     packageApp()                   
	 generateWebXml()        
}                     
  
task( createConfig: "Creates the configuration object") {
   def configFile = new File("${basedir}/grails-app/conf/Config.groovy") 
   def configSlurper = new ConfigSlurper(grailsEnv)
   if(configFile.exists()) { 
		try {
			config = configSlurper.parse(configFile.toURL())
			ConfigurationHolder.setConfig(config)			
		}   
		catch(Exception e) {
			event("StatusFinal", "Failed to compile configuration file $configFile: ${e.message}")
			exit(1)
		}

   } 
   def dataSourceFile = new File("${basedir}/grails-app/conf/DataSource.groovy")
   if(dataSourceFile.exists()) {
		try {
		   def dataSourceConfig = configSlurper.parse(dataSourceFile.toURL())
		   config.merge(dataSourceConfig)
		   ConfigurationHolder.setConfig(config)
		}
		catch(Exception e) {
			event("StatusFinal", "Failed to compile data source file $dataSourceFile: ${e.message}")
			exit(1)
		}
   }
}    
task( packageApp : "Implementation of package task") {
	depends(createStructure,compile, createConfig)
	copyDependencies()
	Ant.mkdir(dir:"${basedir}/web-app/WEB-INF/grails-app/i18n")
	
	if(!GrailsUtil.isDevelopmentEnv()) {
		Ant.mkdir(dir:"${basedir}/web-app/WEB-INF/grails-app/views")		
	    Ant.copy(todir:"${basedir}/web-app/WEB-INF/grails-app/views") {
			fileset(dir:"${basedir}/grails-app/views", includes:"**")
		} 
		packageTemplates()   						
	}	   
	if(config.grails.enable.native2ascii == true) {
		Ant.native2ascii(src:"${basedir}/grails-app/i18n",
						 dest:"${basedir}/web-app/WEB-INF/grails-app/i18n",
						 includes:"*.properties",
						 encoding:"UTF-8")   		
	}                                        
	else {
	    Ant.copy(todir:"${basedir}/web-app/WEB-INF/grails-app/i18n") {
			fileset(dir:"${basedir}/grails-app/i18n", includes:"*.properties")
		}							
	}
    Ant.copy(todir:"${basedir}/web-app/WEB-INF/spring") {
		fileset(dir:"${basedir}/spring", includes:"**")
	}					
    Ant.copy(todir:"${basedir}/web-app/WEB-INF/classes") {
		fileset(dir:"${basedir}", includes:"application.properties")
	}					
	Ant.copy(todir:"${basedir}/web-app/WEB-INF/classes") {
		fileset(dir:"${basedir}/grails-app/conf", includes:"**", excludes:"*.groovy, log4j*")		
		fileset(dir:"${basedir}/hibernate", includes:"**/**")
		fileset(dir:"${basedir}/src/java") {
			include(name:"**/**")
			exclude(name:"**/*.java")
		}
	}           

	def logDest = new File("${basedir}/web-app/WEB-INF/classes/log4j.properties")

    def log4jConfig = config.log4j          
	try {
	 	if(log4jConfig) {       
			def props = log4jConfig.toProperties("log4j")
			logDest.withOutputStream { out ->
				props.store(out, "Grails' Log4j Configuration")
			}
		}		
	}   
	catch(Exception e) {    
		println e.message
		 e.printStackTrace()
	}

}   

DEPENDENCIES = [
"ejb-3.0-persistence.jar",
"ant.jar",  
"hibernate3.jar",
"jdbc2_0-stdext.jar",
"jta.jar",
"groovy-all-*.jar",
"springmodules-sandbox.jar",
"spring-webflow.jar",
"spring-binding.jar",
"standard-${servletVersion}.jar",
"jstl-${servletVersion}.jar",          
"antlr-*.jar",
"cglib-*.jar",
"dom4j-*.jar", 
"ehcache-*.jar", 
"junit-*.jar", 
"commons-logging-*.jar",
"sitemesh-*.jar",
"spring-*.jar",
"commons-lang-*.jar",
"log4j-*.jar",
"ognl-*.jar",
"hsqldb-*.jar",
"commons-collections-*.jar",
"commons-beanutils-*.jar",
"commons-pool-*.jar",
"commons-dbcp-*.jar",
"commons-cli-*.jar",
"commons-validator-*.jar",
"commons-fileupload-*.jar",
"commons-io-*.jar", 
"commons-io-*.jar",  
"*oro-*.jar",    
"jaxen-*.jar"
]    
JAVA_5_DEPENDENCIES = [        
"hibernate-annotations.jar",
"ejb3-persistence.jar",	
]                                      

task( copyDependencies : "Copies the necessary dependencies (jar files) into the lib dir") {
	Ant.sequential {
		mkdir(dir:"${basedir}/web-app/WEB-INF/lib")
		mkdir(dir:"${basedir}/web-app/WEB-INF/spring")
		mkdir(dir:"${basedir}/web-app/WEB-INF/tld")
		copy(todir:"${basedir}/web-app/WEB-INF/lib") {
			fileset(dir:"${grailsHome}/lib") {
				for(d in DEPENDENCIES) {
					include(name:d)
				}
				if(antProject.properties."ant.java.version" == "1.5") {
					for(d in JAVA_5_DEPENDENCIES) {
						include(name:d)
					}
				}				
			}  
			fileset(dir:"${basedir}/lib")
		}   
	}
}

task( generateWebXml : "Generates the web.xml file") {                
	depends(classpath)
	
    new File( "${basedir}/web-app/WEB-INF/web.xml" ).withWriter { w ->   

        def classLoader = new GroovyClassLoader(parentLoader,compConfig,true)
                                                                                                                                             
        pluginManager = new DefaultGrailsPluginManager(pluginResources as Resource[], new DefaultGrailsApplication(new Class[0], classLoader))
    	PluginManagerHolder.setPluginManager(pluginManager)

    	def webXml = resolver.getResource("file:${basedir}/web-app/WEB-INF/web.template.xml")
		try {
    		pluginManager.loadPlugins()  			
	    	pluginManager.doWebDescriptor(webXml, w)			
		}   
		catch(Exception e) {
            event("StatusError", [ e.message ])
			e.printStackTrace(System.out)
		}
    }
}      

task(packageTemplates: "Packages templates into the app") {  
	Ant.mkdir(dir:scaffoldDir)
	if(new File("${basedir}/src/templates/scaffolding").exists()) {
		Ant.copy(todir:scaffoldDir, overwrite:true) {
			fileset(dir:"${basedir}/src/templates/scaffolding", includes:"**")
		}			
	}   
	else {   
		Ant.copy(todir:scaffoldDir, overwrite:true) {
			fileset(dir:"${grailsHome}/src/grails/templates/scaffolding", includes:"**")
		}			
	}
	
}

