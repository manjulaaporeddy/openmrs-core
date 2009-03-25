/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openmrs.Cohort;
import org.openmrs.annotation.Authorized;
import org.openmrs.report.EvaluationContext;
import org.openmrs.report.RenderingMode;
import org.openmrs.report.ReportData;
import org.openmrs.report.ReportRenderer;
import org.openmrs.report.ReportSchema;
import org.openmrs.report.ReportSchemaXml;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains methods pertaining to creating/updating/deleting/retiring/registering/evaluating
 * ReportSchema, ReportSchemaXml, ReportRenderer, ReportXmlMacros, and other 'Report' objects.<br/>
 */
@Transactional
public interface ReportService {
	
	/**
	 * This method evaluates a ReportSchema object for the given EvaluationContext and input Cohort.
	 * It returns the ReportData object which contains the Report "results".
	 * 
	 * @param reportSchema - The {@link ReportSchema} is the main report definition, and contains
	 *            all indicator and required parameter definitions
	 * @param inputCohort - If not null, this will limit the Report evaluation to only those
	 *            patients in this {@link Cohort}. If null, all patients are evaluated.
	 * @param context - The {@link EvaluationContext} which contains the parameters, provides
	 *            caching for the report evaluation
	 * @return {@link ReportData} - Contains the evaluated report data
	 * @throws APIException
	 */
	@Authorized( { OpenmrsConstants.PRIV_RUN_REPORTS })
	public ReportData evaluate(ReportSchema reportSchema, Cohort inputCohort, EvaluationContext context);
	
	/**
	 * Return a list of {@link ReportSchema}s
	 * 
	 * @return a List<ReportSchema> object containing all of the {@link ReportSchema}s
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public List<ReportSchema> getReportSchemas() throws APIException;
	
	/**
	 * Get the {@link ReportSchema} with the given id
	 * 
	 * @param reportSchemaId The Integer ReportSchema id
	 * @return the matching {@link ReportSchema} object
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public ReportSchema getReportSchema(Integer reportSchemaId) throws APIException;
	
	/**
	 * Returns a ReportSchema object from a ReportSchemaXml definition De-serialized the xml
	 * definition, applies macro definitions, and returns an expanded report schema object
	 * 
	 * @param reportSchemaXml - the ReportSchemaXml to use to return a ReportSchema instance
	 * @return ReportSchema
	 * @throws Exception if conversion fails
	 */
	public ReportSchema getReportSchema(ReportSchemaXml reportSchemaXml) throws APIException;
	
	/**
	 * Save or update the given <code>ReportSchema</code> in the database. If this is a new
	 * ReportSchema, the returned ReportSchema will have a new
	 * {@link ReportSchema#getReportSchemaId()} inserted into it that was generated by the database
	 * 
	 * @param reportSchema The <code>ReportSchema</code> to save or update
	 * @throws APIException
	 */
	public void saveReportSchema(ReportSchema reportSchema) throws APIException;
	
	/**
	 * Deletes a <code>ReportSchema</code> from the database.
	 * 
	 * @param reportSchema The <code>ReportSchema</code> to remove from the system
	 * @throws APIException
	 */
	public void deleteReportSchema(ReportSchema reportSchema);
	
	/**
	 * Returns a Collection<ReportRenderer> of all registered ReportRenderers
	 * 
	 * @return All registered report renderers
	 */
	@Transactional(readOnly = true)
	public Collection<ReportRenderer> getReportRenderers();
	
	/**
	 * Returns a List of {@link RenderingMode}s that the passed {@link ReportSchema} supports, in
	 * their preferred order
	 * 
	 * @return all rendering modes for the given schema, in their preferred order
	 */
	@Transactional(readOnly = true)
	public List<RenderingMode> getRenderingModes(ReportSchema schema);
	
	/**
	 * Returns the registered {@link ReportRenderer} whose class matches the passed class
	 * 
	 * @param clazz The ReportRenderer implementation class to retrieve
	 * @return - The {@link ReportRenderer} that has been registered that matches the passed class
	 */
	@Transactional(readOnly = true)
	public ReportRenderer getReportRenderer(Class<? extends ReportRenderer> clazz);
	
	/**
	 * Returns the registered {@link ReportRenderer} whose class matches the passed class name
	 * 
	 * @param className The String name of the ReportRenderer implementation class to retrieve
	 * @return The {@link ReportRenderer} that has been registered that matches the passed class
	 *         name
	 */
	@Transactional(readOnly = true)
	public ReportRenderer getReportRenderer(String className);
	
	/**
	 * Add the given map to this service's renderers This map is set via spring, see the
	 * applicationContext-service.xml file
	 * 
	 * @param renderers Map of class to renderer object
	 */
	public void setRenderers(Map<Class<? extends ReportRenderer>, ReportRenderer> renderers) throws APIException;
	
	/**
	 * Gets the renderers map registered to this report service
	 * 
	 * @return Map of registered {@link org.openmrs.report#ReportRenderer}s
	 * @throws APIException
	 */
	@Transactional(readOnly = true)
	public Map<Class<? extends ReportRenderer>, ReportRenderer> getRenderers() throws APIException;
	
	/**
	 * Registers the given renderer with the service
	 * 
	 * @param rendererClass
	 * @param renderer
	 * @throws APIException
	 */
	public void registerRenderer(Class<? extends ReportRenderer> rendererClass, ReportRenderer renderer) throws APIException;
	
	/**
	 * Convenience method for {@link #registerRenderer(Class, ReportRenderer)}
	 * 
	 * @param rendererClass
	 * @throws APIException
	 */
	public void registerRenderer(String rendererClass) throws APIException;
	
	/**
	 * Remove the renderer associated with <code>rendererClass</code> from the list of available
	 * renderers
	 * 
	 * @param rendererClass
	 */
	public void removeRenderer(Class<? extends ReportRenderer> rendererClass) throws APIException;
	
	/**
	 * Get the xmlified ReportSchema object that was saved previously
	 * 
	 * @return ReportSchemaXml object that is associated with the given id
	 */
	@Transactional(readOnly = true)
	public ReportSchemaXml getReportSchemaXml(Integer reportSchemaXmlId);
	
	/**
	 * Insert or update the given ReportSchemaXml object in the database.
	 * 
	 * @param reportSchemaXml xml to save
	 */
	public void saveReportSchemaXml(ReportSchemaXml reportSchemaXml);
	
	/**
	 * Create a new ReportSchemaXml object in the database.
	 * 
	 * @param reportSchemaXml xml to save
	 * @deprecated use saveReportSchemaXml(reportSchemaXml)
	 */
	public void createReportSchemaXml(ReportSchemaXml reportSchemaXml);
	
	/**
	 * Update the given ReportSchemaXml object in the database.
	 * 
	 * @param reportSchemaXml xml to save
	 * @deprecated use saveReportSchemaXml(reportSchemaXml)
	 */
	public void updateReportSchemaXml(ReportSchemaXml reportSchemaXml);
	
	/**
	 * Delete the given ReportSchemaXml class from the db
	 */
	public void deleteReportSchemaXml(ReportSchemaXml reportSchemaXml);
	
	/**
	 * Get all saved ReportSchemaXml objects in the db
	 * 
	 * @return List of ReportSchemaXml objects
	 */
	@Transactional(readOnly = true)
	public List<ReportSchemaXml> getReportSchemaXmls();
	
	/**
	 * Gets the macros that will be used when deserializing ReportSchemaXML
	 * 
	 * @return macros
	 */
	@Transactional(readOnly = true)
	public Properties getReportXmlMacros();
	
	/**
	 * Saves the macros that will be used when deserializing ReportSchemaXML
	 * 
	 * @param macros the macros to set
	 */
	public void saveReportXmlMacros(Properties macros);
	
	/**
	 * Applies the report xml macros to the input, and returns it.
	 * 
	 * @param input The text (presumably a report schema xml definition) that you want to apply
	 *            macros to
	 * @return the result of applying macro substitutions to input
	 */
	public String applyReportXmlMacros(String input);
	
}
