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
package org.sentilo.web.catalog.domain;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Permission implements CatalogDocument{
	
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	private String id;
	private String source;
	private String target;	
	private Type type;
	@JsonIgnore
	private Date createdAt;	
	@JsonIgnore
	private Date updateAt;

	public enum Type {
		READ, WRITE;
	}	

	public Permission() {		
	}
	
	public Permission(String source) {
		this(source, source, Type.WRITE);
	}
	
	public Permission(String source, String target, Type type) {
		this.source = source;
		this.target = target;
		this.type = type;
		buildId();
		
	}
	
	private void buildId(){
		//El identificador interno de un permiso esta formado por la concatenacion del source y el target
		this.id = this.source+"@"+this.target;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Permission)) {
			return false;
		}
		Permission other = (Permission) obj;
		return source.equals(other.source) && target.equals(other.target);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());		
		return result*super.hashCode();
	}
	

	@Override
	public String toString() {
		return String.format("Permission. Source: %s Target: %s Type: %s",	source, target, type);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}
