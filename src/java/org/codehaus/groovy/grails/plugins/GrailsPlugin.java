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
package org.codehaus.groovy.grails.plugins;

import grails.util.BuildScope;
import grails.util.Environment;
import groovy.lang.GroovyObject;
import groovy.util.slurpersupport.GPathResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.spring.RuntimeSpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.type.filter.TypeFilter;

/**
 * <p>Plugin interface that adds Spring {@link org.springframework.beans.factory.config.BeanDefinition}s
 * to a registry based on a {@link GrailsApplication} object. After all <code>GrailsPlugin</code> classes
 * have been processed the {@link org.springframework.beans.factory.config.BeanDefinition}s in the registry are
 * loaded in a Spring {@link org.springframework.context.ApplicationContext} that's the singular
 * configuration unit of Grails applications.</p>
 *
 * <p>It's up to implementation classes to determine where <code>GrailsPlugin</code> instances are loaded
 * from.</p>
 *
 * @author Steven Devijver
 * @author Graeme Rocher
 *
 * @since 0.2
 * @see BeanDefinitionRegistry
 */
@SuppressWarnings("rawtypes")
public interface GrailsPlugin extends ApplicationContextAware, Comparable, GrailsPluginInfo {

    int EVENT_ON_CHANGE = 0;
    int EVENT_ON_CONFIG_CHANGE = 1;
    int EVENT_ON_SHUTDOWN = 2;

    String DO_WITH_DYNAMIC_METHODS = "doWithDynamicMethods";

    /**
     * The scopes to which this plugin applies
     */
    String SCOPES = "scopes";

    /**
     * The environments to which this plugin applies
     */
    String ENVIRONMENTS = "environments";

    /**
     * The prefix used in plug-ins paths
     */
    String PLUGINS_PATH = "/plugins";

    /**
     * Defines the name of the property that specifies resources which this plugin monitors for changes
     * in the format a Ant-style path
     */
    String WATCHED_RESOURCES = "watchedResources";

    /**
     * Defines the name of the property that specifies a List or plugins that this plugin evicts
     * Eviction occurs when the PluginManager loads
     */
    String EVICT = "evict";

    /**
     * The status of the plugin.
     */
    String STATUS = "status";

    /**
     * When a plugin is "enabled" it will be loaded as usual.
     */
    String STATUS_ENABLED = "enabled";

    /**
     * When a plugin is "disabled" it will not be loaded
     */
    String STATUS_DISABLED = "disabled";

    /**
     * Defines the name of the property that defines a list of plugin names that this plugin influences.
     * A influenced plugin will be refreshed (@see refresh()) when a watched resource changes
     */
    String INFLUENCES = "influences";

    /**
     * Defines the name of the property that defines the closure that will be invoked
     * when a watched resource changes
     */
    String ON_CHANGE = "onChange";

    /**
     * Defines the name of the property that holds a closure to be invoked when shutdown is called
     */
    String ON_SHUTDOWN = "onShutdown";

    /**
     * Defines the name of the property that defines the closure that will be invoked
     * when a the Grails configuration object changes
     */
    String ON_CONFIG_CHANGE = "onConfigChange";

    /**
     * Defines the name of the property that defines the closure that will be invoked
     * when the web.xml is being generated
     */
    String DO_WITH_WEB_DESCRIPTOR = "doWithWebDescriptor";

    /**
     * Defines the name of the property that defines the closure that will be invoked during runtime spring configuration
     */
    String DO_WITH_SPRING = "doWithSpring";

    /**
     * Defines the name of the property that defines a closure that will be invoked after intialisation
     * and when the application context has been built
     */
    String DO_WITH_APPLICATION_CONTEXT = "doWithApplicationContext";

    /**
     * Defines the name of the property that specifies which plugins this plugin depends on
     */
    String DEPENDS_ON = "dependsOn";

    /**
     * Define the list of ArtefactHandlers supporting by the plugin
     */
    String ARTEFACTS = "artefacts";

    /**
     * The name of the property that provides a list of shipped, but overridable artefactssw
     */
    String PROVIDED_ARTEFACTS = "providedArtefacts";

    /**
     * The name of the property that provides a list of plugins this plugin should load before
     */
    String PLUGIN_LOAD_BEFORE_NAMES = "loadBefore";

    /**
     * The name of the property that provides a list of plugins this plugin should after before
     */
    String PLUGIN_LOAD_AFTER_NAMES = "loadAfter";

    /**
     * The field that reperesents the list of resources to exclude from plugin packaging
     */
    String PLUGIN_EXCLUDES = "pluginExcludes";

    /**
     * The field that reperesents the list of type filters a plugin provides
     */
    String TYPE_FILTERS = "typeFilters";

    /**
     * <p>This method is called to allow the plugin to add {@link org.springframework.beans.factory.config.BeanDefinition}s
     * to the {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}.</p>
     *
     * @param applicationContext The Spring ApplicationContext instance
     */
    void doWithApplicationContext(ApplicationContext applicationContext);

    /**
     * Executes the plugin code that performs runtime configuration as defined in the doWithSpring closure
     *
     * @param springConfig The RuntimeSpringConfiguration instance
     */
    void doWithRuntimeConfiguration(RuntimeSpringConfiguration springConfig);

    /**
     * Handles processing of web.xml. The method is passed a GPathResult which is parsed by
     * groovy.util.XmlSlurper. A plug-in can then manipulate the in-memory XML however it chooses
     * Once all plug-ins have been processed the web.xml is then written to disk based on its in-memory form
     *
     * @param webXml The GPathResult representing web.xml
     */
    void doWithWebDescriptor(GPathResult webXml);

    /**
     * Makes the plugin excluded for a particular BuildScope
     * @param buildScope The BuildScope
     */
    void addExclude(BuildScope buildScope);

    /**
     * Makes the plugin excluded for a particular Environment
     * @param env The Environment
     */
    void addExclude(Environment env);

    /**
     * Return whether this plugin supports the given PluginScope
     *
     * @param buildScope The PluginScope
     * @return True if it does
     */
    boolean supportsScope(BuildScope buildScope);

    /**
     * Returns whether this plugin supports the given environment name
     * @param environment The environment name
     * @return True if it does
     */
    boolean supportsEnvironment(Environment environment);

    /**
     * @return True if the current plugin supports the current BuildScope and Environment
     */
    boolean supportsCurrentScopeAndEnvironment();

    /**
     * Write some documentation to the DocumentationContext
     */
    void doc(String text);

    /**
     * Returns the path of the plug-in
     *
     * @return A String that makes up the path to the plug-in in the format /plugins/PLUGIN_NAME-PLUGIN_VERSION
     */
    String getPluginPath();

    /**
     * @return The names of the plugins this plugin is dependant on
     */
    String[] getDependencyNames();

    /**
     * @return The names of the plugins this plugin should evict onload
     */
    String[] getEvictionNames();

    /**
     * Retrieves the names of plugins that this plugin should be loaded after. This differs
     * from dependencies in that if that plugin doesn't exist this plugin will still be loaded.
     * It is a way of enforcing plugins are loaded before, but not necessarily needed
     *
     * @return The names of the plugins that this plugin should be loaded after
     */
    String[] getLoadAfterNames();

    /**
     * Retrieves the names of plugins that this plugin should be loaded before. As with getLoadAfterNames() it
     * is not a requirement that the specified plugins exist
     *
     * @return The names of the plugins that this plugin should load before
     */
    String[] getLoadBeforeNames();

    /**
     * The version of the specified dependency
     *
     * @param name the name of the dependency
     * @return The version
     */
    String getDependentVersion(String name);

    /**
     * When called this method checks for any changes to the plug-ins watched resources
     * and reloads appropriately
     *
     * @return Returns true when the plug-in itself changes in some way, as oppose to plug-in resources
     */
    boolean checkForChanges();

    /**
     * Refreshes this Grails plugin reloading any watched resources as necessary
     */
    void refresh();

    /**
     * Retrieves the plugin manager if known, otherwise returns null
     * @return The PluginManager or null
     */
    GrailsPluginManager getManager();

    /**
     * Retrieves the wrapped plugin instance for this plugin
     * @return The plugin instance
     */
    GroovyObject getInstance();

    /**
     * Sets the plugin manager for this plugin
     *
     * @param manager A GrailsPluginManager instance
     */
    void setManager(GrailsPluginManager manager);

    void setApplication(GrailsApplication application);

    /**
     * Calls a "doWithDynamicMethods" closure that allows a plugin to register dynamic methods at runtime
     * @param applicationContext The Spring ApplicationContext instance
     */
    void doWithDynamicMethods(ApplicationContext applicationContext);

    /**
     * @return Whether the plugin is enabled or not
     */
    boolean isEnabled();

    /**
     * Retrieve the plugin names that this plugin is observing for changes
     *
     * @return The names of the observed plugins
     */
    String[] getObservedPluginNames();

    /**
     * Notifies this plugin of the specified Event calling the onChange listener
     *
     * @param event The event to listen for
     */
    void notifyOfEvent(Map event);

    /**
     * Notifies the plugin of a specific event for the given event id, which is one of ON_CHANGE, ON_CONFIG_CHANGE
     *
     * @param eventKind The event kind
     * @param source The source of the event
     * @return a Map that represents the event
     */
    Map notifyOfEvent(int eventKind, Object source);

    /**
     * Called prior to the initialisation of the GrailsApplication instance to allow the registration
     * of additonal ArtefactHandlers
     *
     * @see org.codehaus.groovy.grails.commons.ArtefactHandler
     *
     */
    void doArtefactConfiguration();

    /**
     * Retrieves an array of provided Artefacts that are pre-compiled additions to the GrailsApplication object
     * but are overridable by the end-user
     *
     * @return A list of provided artefacts
     */
    Class<?>[] getProvidedArtefacts();

    /**
     * Returns the name of the plugin as represented in the file system including the version. For example TagLibGrailsPlugin would result in "tag-lib-0.1"
     * @return The file system representation of the plugin name
     */
    String getFileSystemName();

    /**
     * Returns the name of the plugin as represented on the file system without the version. For example TagLibGrailsPlugin would result in "tag-lib"
     * @return The file system name
     */
    String getFileSystemShortName();

    /**
     * Returns the underlying class that represents this plugin
     * @return The plugin class
     */
    Class<?> getPluginClass();

    /**
     * A list of resources that the plugin should exclude from the packaged distribution
     * @return a List of resources
     */
    List<String> getPluginExcludes();

    /**
     * Returns whether this plugin is loaded from the current plugin. In other words when you execute grails run-app from a plugin project
     * the plugin project's *GrailsPlugin.groovy file represents the base plugin and this method will return true for this plugin
     *
     * @return True if it is the base plugin
     */
    boolean isBasePlugin();

    /**
     * Sets whether this plugin is the base plugin
     *
     * @param isBase True if is
     * @see #isBasePlugin()
     */
    void setBasePlugin(boolean isBase);

    /**
     * Plugin can provide a list of Spring TypeFilters so that annotated components can
     * be scanned into the ApplicationContext
     * @return A collection of TypeFilter instance
     */
    Collection<? extends TypeFilter> getTypeFilters();
}
