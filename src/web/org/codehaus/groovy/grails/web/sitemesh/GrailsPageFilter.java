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
package org.codehaus.groovy.grails.web.sitemesh;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.filter.PageFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Extends the default page filter to overide the apply decorator behaviour
 * if the page is a GSP
 *  
 * @author Graeme Rocher
 * @since Apr 19, 2006
 */
public class GrailsPageFilter extends PageFilter {

	private static final Log LOG = LogFactory.getLog( GrailsPageFilter.class );

	
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);
		this.filterConfig = filterConfig;
        FactoryHolder.setFactory(this.factory);        
    }

   /*
     * TODO: This method has been copied from the parent to fix a bug in sitemesh 2.3. When sitemesh 2.4 is release this method and the two private methods below can removed

     * Main method of the Filter.
     *
     * <p>Checks if the Filter has been applied this request. If not, parses the page
     * and applies {@link com.opensymphony.module.sitemesh.Decorator} (if found).
     */
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) rq;

        if (rq.getAttribute(FILTER_APPLIED) != null || factory.isPathExcluded(extractRequestPath(request))) {
            // ensure that filter is only applied once per request
            chain.doFilter(rq, rs);
        }
        else {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

            factory.refresh();
            DecoratorMapper decoratorMapper = factory.getDecoratorMapper();
            HttpServletResponse response = (HttpServletResponse) rs;

            // parse data into Page object (or continue as normal if Page not parseable)
            Page page = parsePage(request, response, chain);

            if (page != null) {
                page.setRequest(request);

                Decorator decorator = decoratorMapper.getDecorator(request, page);
                if (decorator != null && decorator.getPage() != null) {
                    applyDecorator(page, decorator, request, response);
                    return;
                }
                // if we got here, an exception occured or the decorator was null,
                // what we don't want is an exception printed to the user, so
                // we write the original page
                writeOriginal(request, response, page);
            }
        }
    }

    private String extractRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();
        return (servletPath == null ? "" : servletPath)
                + (pathInfo == null ? "" : pathInfo)
                + (query == null ? "" : ("?" + query));
    }

    /** Write the original page data to the response. */
    private void writeOriginal(HttpServletRequest request, HttpServletResponse response, Page page) throws IOException {
        response.setContentLength(page.getContentLength());
        if (request.getAttribute(USING_STREAM).equals(Boolean.TRUE))
        {
            PrintWriter writer = new PrintWriter(response.getOutputStream());
            page.writePage(writer);
            //flush writer to underlying outputStream
            writer.flush();
            response.getOutputStream().flush();
        }
        else
        {
            page.writePage(response.getWriter());
            response.getWriter().flush();
        }
    }


    /* (non-Javadoc)
	 * @see com.opensymphony.module.sitemesh.filter.PageFilter#applyDecorator(com.opensymphony.module.sitemesh.Page, com.opensymphony.module.sitemesh.Decorator, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void applyDecorator(Page page, Decorator decorator, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String uriPath = decorator.getURIPath();
        if(uriPath != null && uriPath.endsWith(".gsp")) {
    		request.setAttribute(PAGE, page);
                      
            RequestDispatcher rd = request.getRequestDispatcher(decorator.getURIPath());
            if(!response.isCommitted()) {
                if(LOG.isDebugEnabled()) {
                	LOG.debug("Rendering layout using forward: " + decorator.getURIPath());
                }
                rd.forward(request, response);
            } 
            else {
                if(LOG.isDebugEnabled()) {
                	LOG.debug("Rendering layout using include: " + decorator.getURIPath());
                }
                request.setAttribute(GrailsApplicationAttributes.GSP_TO_RENDER,decorator.getURIPath());
                rd.include(request,response);
                request.removeAttribute(GrailsApplicationAttributes.GSP_TO_RENDER);
            }
            
            // set the headers specified as decorator init params
            while (decorator.getInitParameterNames().hasNext()) {
                String initParam = (String) decorator.getInitParameterNames().next();
                if (initParam.startsWith("header.")) {
                    response.setHeader(initParam.substring(initParam.indexOf('.')), decorator.getInitParameter(initParam));
                }
            }
            request.removeAttribute(PAGE);        		        		
    	}
    	else {
    		
    		super.applyDecorator(page, decorator, request, response);
    		
    	}
	}

}
