/*
 * Copyright 2009 the original author or authors.
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
package org.codehaus.groovy.grails.test

import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher

/**
 * Describes the contract that a test type must support to be 
 * runnable by `grails test-app`.
 * 
 * There is expected to be a «type»TestsPreparation() available in the 
 * script binding for the test infrastructure that returns an object that 
 * supports this contract.
 */
interface GrailsTestTypeRunner {
        
    /**
     * The number of tests that may be run.
     * 
     * This is only used for display, so does not need to be precise.
     */
    int getTestCount() 
    
    /**
     * The class loader that was used to load the tests.
     */
    ClassLoader getTestClassLoader() 
    
    /**
     * Runs the tests.
     */
    GrailsTestTypeResult run(GrailsTestEventPublisher eventPublisher)
}
