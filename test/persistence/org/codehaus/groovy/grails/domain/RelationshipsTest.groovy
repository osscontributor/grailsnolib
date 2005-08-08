package org.codehaus.groovy.grails.domain;

class RelationshipsTest {
  @Property Map relationships = [ "ones" : OneToManyTest2.class ];
   
   @Property Long id;
   @Property Long version;

   //@Property Set manys; // many-to-many relationship
   @Property OneToOneTest one; // uni-directional one-to-one
   @Property Set ones; // uni-directional one-to-many relationship    
}


