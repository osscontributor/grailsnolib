// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

if(System.properties["${appName}.config.location"]) {
    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// log4j configuration
log4j {
    appender.stdout = "org.apache.log4j.ConsoleAppender"
	appender.'stdout.layout'="org.apache.log4j.PatternLayout"
 	appender.'stdout.layout.ConversionPattern'='[%r] %c{2} %m%n'
    rootLogger="error,stdout"
    logger {
        grails="error"
        org {
            codehaus.groovy.grails.web.servlet="error"  //  controllers
            codehaus.groovy.grails.web.errors="error"  //  web layer errors            
			codehaus.groovy.grails.web.pages="error" //  GSP
        	codehaus.groovy.grails.web.sitemesh="error" //  layouts
        	codehaus.groovy.grails."web.mapping.filter"="error" // URL mapping
        	codehaus.groovy.grails."web.mapping"="error" // URL mapping
            codehaus.groovy.grails.commons="info" // core / classloading
            codehaus.groovy.grails.plugins="error" // plugins
            codehaus.groovy.grails.orm.hibernate="error" // hibernate integration
            springframework="error"
            hibernate="error"
        }
    }
}
