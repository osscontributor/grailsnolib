package org.codehaus.groovy.grails.orm.hibernate;

import org.codehaus.groovy.grails.commons.*
import org.codehaus.groovy.grails.commons.test.*

class RelationshipManagementMethodsTests extends org.codehaus.groovy.grails.plugins.web.AbstractGrailsPluginTests {

	void testWithMapAddition() {
		def personClass = ga.getDomainClass("Person")
		def addressClass = ga.getDomainClass("Address")

		def p = personClass.newInstance()

		p.name = "Homer Simpson"


        p.addToAddresses(number:"22")
        def address = p.addresses.iterator().next()
        assert address
        assertEquals "22", address.number
        assertEquals p, address.person

        p.removeFromAddresses(address)

        assertFalse p.addresses.contains(address)
        assert !address.person
	}

	void testWithInstanceAddition() {
		def personClass = ga.getDomainClass("Person")
		def addressClass = ga.getDomainClass("Address")

		def p = personClass.newInstance()

		p.name = "Homer Simpson"



        def address = addressClass.newInstance()
        address.number = "22"

        p.addToAddresses(address)
        
        assertTrue p.addresses.contains(address)

        assert address
        assertEquals "22", address.number
        assertEquals p, address.person

        p.removeFromAddresses(address)

        assertFalse p.addresses.contains(address)
        assert !address.person

	}

	void testWithManyToManyRelationship() {
	    def bookmarkClass = ga.getDomainClass("Bookmark")
	    def tagClass = ga.getDomainClass("Tag")

        def bookmark = bookmarkClass.newInstance()
        bookmark.url='http://www.ru'

        def tag = tagClass.newInstance()
        tag.name = "foo"

        bookmark.addToTags(tag)

        assert bookmark.tags.contains(tag)
        assert tag.bookmarks.contains(bookmark)

        bookmark.removeFromTags(tag)
        assert !bookmark.tags.contains(tag)
        assert !tag.bookmarks.contains(bookmark)

    }


	void onSetUp() {

		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.CoreGrailsPlugin")
		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.datasource.DataSourceGrailsPlugin")
		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.i18n.I18nGrailsPlugin")
		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin")
		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.orm.hibernate.HibernateGrailsPlugin")
		pluginsToLoad << gcl.loadClass("org.codehaus.groovy.grails.plugins.web.ControllersGrailsPlugin")

		this.gcl.parseClass('''
class Person {
	Long id
	Long version
	String name
	Set addresses
	static hasMany = [addresses:Address]
}
class Address {
	Long id
	Long version
	String number
	Person person
}
class Bookmark {
	Long id
	Long version

    String url
    Set tags = new HashSet()
    static hasMany = [tags:Tag]
    static belongsTo = [Tag]
}
class Tag {
	Long id
	Long version

    String name
    Set bookmarks = new HashSet()
    static hasMany = [bookmarks:Bookmark]
}
'''
		)
	}
	
	void onTearDown() {
		
	}
}
