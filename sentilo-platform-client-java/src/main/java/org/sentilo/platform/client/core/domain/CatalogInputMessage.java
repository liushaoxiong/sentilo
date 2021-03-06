/*
 * Sentilo
 *   
 * Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de  Barcelona.
 *   
 * This program is licensed and may be used, modified and redistributed under the
 * terms  of the European Public License (EUPL), either version 1.1 or (at your 
 * option) any later version as soon as they are approved by the European 
 * Commission.
 *   
 * Alternatively, you may redistribute and/or modify this program under the terms
 * of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either  version 3 of the License, or (at your option) any later 
 * version. 
 *   
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. 
 *   
 * See the licenses for the specific language governing permissions, limitations 
 * and more details.
 *   
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along 
 * with this program; if not, you may find them at: 
 *   
 *   https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *   http://www.gnu.org/licenses/ 
 *   and 
 *   https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.platform.client.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.sentilo.common.domain.CatalogComponent;
import org.sentilo.common.domain.CatalogSensor;
import org.sentilo.platform.client.core.utils.ResourcesUtils;



public class CatalogInputMessage implements PlatformClientInputMessage{
	
	@JsonIgnore
	private String providerId;	
	
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	private List<CatalogSensor> sensors;
	
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	private List<CatalogComponent> components;
	
	/** Lista ordenada de los identificadores que forman el path del recurso. */
	@JsonIgnore
	private final List<String> resourcesValues = new ArrayList<String>();
	
	@JsonIgnore
	private String identityToken;
	
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	private Map<String,String> parameters;
	
	public CatalogInputMessage(){
		super();
	}
	
	public CatalogInputMessage(String providerId){
	  this();
	  this.providerId = providerId;
	  this.sensors = new ArrayList<CatalogSensor>();
	  ResourcesUtils.addToResources(this.providerId, getResourcesValues());
	}
	
	public CatalogInputMessage(String providerId, List<CatalogSensor> sensors){
	  this(providerId);
	  this.sensors = sensors;
	}
		
	public List<String> getResourcesValues() {
		return resourcesValues;
	}


	public String getIdentityToken() {
		return identityToken;
	}

	
	public void setIdentityToken(String identityToken) {
		this.identityToken = identityToken;
	}
			
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public void setSensors(List<CatalogSensor> sensors) {
		this.sensors = sensors;
	}

	public List<CatalogSensor> getSensors() {
		return sensors;
	}

	public void setComponents(List<CatalogComponent> components) {
		this.components = components;
	}

	public List<CatalogComponent> getComponents() {
		return components;
	}

	public void setParameters(Map<String,String> parameters) {
		this.parameters = parameters;
	}

	public Map<String,String> getParameters() {
		return parameters;
	}
	
}
