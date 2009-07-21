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
package org.codehaus.groovy.grails.web.taglib;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.grails.commons.TagLibArtefactHandler;
import org.codehaus.groovy.grails.web.pages.GroovyPage;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * A closure that represents the body of a tag and captures its output returning the result when invoked
 *
 * @author Graeme Rocher
 * @since 0.5
 *
 *        <p/>
 *        Created: Apr 19, 2007
 *        Time: 2:21:38 PM
 */
public class GroovyPageTagBody extends Closure {
    private Closure bodyClosure;
    private GrailsWebRequest webRequest;
    private Binding binding;
    private static final String BLANK_STRING = "";
    private boolean writeStringResult = false;

    public GroovyPageTagBody(Object owner, GrailsWebRequest webRequest,Closure bodyClosure) {
        this(owner, webRequest, false, bodyClosure);
    }

    public GroovyPageTagBody(Object owner, GrailsWebRequest webRequest,boolean writeStringResult, Closure bodyClosure) {
        super(owner);

        if(bodyClosure == null) throw new IllegalStateException("Argument [bodyClosure] cannot be null!");
        if(webRequest == null) throw new IllegalStateException("Argument [webRequest] cannot be null!");

        this.bodyClosure = bodyClosure;
        this.webRequest = webRequest;
        binding = null;
        if(owner instanceof GroovyPage)
            binding = ((GroovyPage) owner).getBinding();
        else if(owner != null && owner.getClass().getName().endsWith(TagLibArtefactHandler.TYPE)) {
            binding = (Binding) ((GroovyObject)owner).getProperty(GroovyPage.PAGE_SCOPE);
        }

        this.writeStringResult=writeStringResult;
    }


    private Object captureClosureOutput(Object args) {
        Writer originalOut = webRequest.getOut();

        try {
            final GroovyPageTagWriter capturedOut = createWriter();


            Object bodyResult;

            if(args!=null) {
                if(args instanceof Map) {
                    // The body can be passed a set of variables as a map that
                    // are then made available in the binding. This allows the
                    // contents of the body to reference any of these variables
                    // directly.
                    //
                    // For example, body(foo: 1, bar: 'test') would allow this
                    // GSP fragment to work:
                    //
                    //   <td>Foo: ${foo} and bar: ${bar}</td>
                    //
                    // Note that any variables with the same name as one of the
                    // new ones will be overridden for the scope of the host
                    // tag's body.

                    // GRAILS-2675: Copy the current binding so that we can restore
                    // it to its original state.
                    Map currentBinding = null;
                    Map originalBinding = null;

                    if(binding!=null) {
                        currentBinding = binding.getVariables();
                        originalBinding = new HashMap(currentBinding);
                        // Add the extra variables passed into the body to the
                        // current binding.
                        currentBinding.putAll((Map) args);
                    }


                    try {
                        bodyResult = bodyClosure.call(args);
                    }
                    finally {
                        if(binding!=null) {
                            // GRAILS-2675: Restore the original binding.
                            currentBinding.clear();
                            currentBinding.putAll(originalBinding);
                        }
                    }
                }
                else {
                    bodyResult = bodyClosure.call(args);
                }
            }
            else {
                bodyResult = bodyClosure.call();
            }
            String output = capturedOut.getValue();
            if(org.apache.commons.lang.StringUtils.isBlank(output)) {
                if(bodyResult instanceof String) {
                    return bodyResult;
                }
                else if(bodyResult instanceof GString) {
                    return bodyResult.toString();
                }
                else {
                    return BLANK_STRING;
                }
            }

            return output;
        } finally {
            if(binding!=null) {
                binding.setVariable(GroovyPage.OUT, originalOut);
            }
            webRequest.setOut(originalOut);
        }
    }

    private GroovyPageTagWriter createWriter() {

        GroovyPageTagWriter out = new GroovyPageTagWriter();
        if(binding!=null) {
            binding.setVariable(GroovyPage.OUT, out);
        }
        webRequest.setOut(out);

        return out;
    }

    public Object doCall() {
        return captureClosureOutput(null);
    }

    public Object doCall(Object arguments) {
        return captureClosureOutput(arguments);
    }

    public Object call() {
        return captureClosureOutput(null);
    }

    public Object call(Object[] args) {
        return captureClosureOutput(args);
    }

    public Object call(Object arguments) {
        return captureClosureOutput(arguments);
    }
}
