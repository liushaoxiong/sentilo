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
package org.sentilo.web.catalog.service.impl;

import java.util.Collection;
import java.util.Set;

import org.sentilo.web.catalog.domain.ComponentType;
import org.sentilo.web.catalog.repository.ComponentTypesRepository;
import org.sentilo.web.catalog.service.ComponentTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ComponentTypesServiceImpl extends AbstractBaseServiceImpl<ComponentType> implements ComponentTypesService {

	@Autowired
	private ComponentTypesRepository repository;
		

	public ComponentTypesServiceImpl() {
		super(ComponentType.class);
	}

	@Override
	public ComponentTypesRepository getRepository() {
		return repository;
	}

	public void setRepository(ComponentTypesRepository repository) {
		this.repository = repository;
	}

	@Override
	public String getEntityId(ComponentType entity) {
		return entity.getId();
	}

	@Override
	//@CacheEvict(value="componentTypes", beforeInvocation=true, key="#entity.id")
	public ComponentType update(ComponentType entity) {		
		return super.update(entity);
	}

	@Override
	//@CacheEvict(value="componentTypes", beforeInvocation=true, key="#entity.id")
	public void delete(ComponentType entity) {
		super.delete(entity);
	}

	@Override
	//@CacheEvict(value="componentTypes", allEntries=true)
	public void delete(Collection<ComponentType> entities) {
		super.delete(entities);
	}

	@Override
	//@Cacheable(value="componentTypes", key="#entity.id")
	public ComponentType find(ComponentType entity) {
		return super.find(entity);
	}

	@Override
	public void insertNewComponentTypesIfNotExists(Set<String> componentTypes) {		
		for(String type : componentTypes){
			if(find(new ComponentType(type)) == null){
				create(new ComponentType(type, type));
			}
		}				
	}		
}
