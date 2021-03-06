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
package org.sentilo.common.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.sentilo.common.rest.RequestParameters;


public abstract class URIUtils {
		
	private URIUtils(){
		throw new AssertionError();
	}
	
	public static String getURI(String host, String path) {
		return getURI(host, path, null);
	}

	public static String getURI(String host, String path, RequestParameters parameters) {
				
		try {
			URI baseURI = getBaseURI(host, path);
			URIBuilder builder = new URIBuilder(baseURI);
			
			if (parameters != null && parameters.size()>0) {
				for (String key : parameters.keySet()) {
					String value = parameters.get(key);
					builder.setParameter(key, value);
				}
			}
			return builder.build().toString();
		} catch (URISyntaxException e) {
			throw buildIllegalArgumentException(host, path, e);
		}
	}

	private static URI getBaseURI(String host, String path) {
		try {
			String baseURI = null;
			if(host.endsWith(SentiloConstants.SLASH) && path.startsWith(SentiloConstants.SLASH)){
				baseURI = host.substring(0, host.length()-1) + path;
			}else if(!host.endsWith(SentiloConstants.SLASH) && !path.startsWith(SentiloConstants.SLASH)){
				baseURI = host + SentiloConstants.SLASH + path;
			}else{
				baseURI = host +  path;
			}
				
		
			URI uri = new URI(baseURI);
			return uri;
		} catch (URISyntaxException e) {
			throw buildIllegalArgumentException(host, path, e);
		} catch(NullPointerException npe){
			throw buildIllegalArgumentException(host, path, npe);
		}
	}
	
	private static IllegalArgumentException buildIllegalArgumentException(String host, String path , Exception cause){
		String templateMessage = "Params host %s and path %s are not valid";
		return new IllegalArgumentException(String .format(templateMessage, host,path), cause);
	}

}
