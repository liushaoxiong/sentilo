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
package org.sentilo.platform.service.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cachea los ids internos en Redis de las diferentes entidades del modelo: proveedores, sensores, ...
 * evitando de esta manera tener que acceder a Redis para recuperarlos. 
 */
@Component
public class JedisSequenceUtils {
	private static final String PID_KEY = "global:pid";	
	private static final String SID_KEY = "global:sid";
	private static final String SDID_KEY = "global:sdid";
	private static final String SOID_KEY = "global:soid";
	private static final String AID_KEY = "global:aid";
	private static final String AMID_KEY = "global:amid";
	public static final String NIL = "nil";
	
	@Autowired
	private JedisTemplate<String,String> jedisTemplate;	

	private Map<String,Long> pids = new HashMap<String, Long>();	
	private Map<String,Long> sids = new HashMap<String, Long>();
	private Map<String,Long> aids = new HashMap<String, Long>();
	
	
	public Long getPid(String providerId){
		
		// Al recuperar un valor de un secuencial, puede pasar:
		// 1. Exista en la cache, y por lo tanto no hace falta acceder a Redis
		// 2. No exista en la cache. Si no existe en la cache, se debe acceder a Redis para recuperar el valor
		//    de la key "provider:"+providerId+":pid". Puede ocurrir:
		//    2.1 Que exista, y por lo tanto se retorne el valor numerico.
		//    2.2 No exista, y por lo tanto se retorne NULL.
		String internalKey = providerId;
		if(!pids.containsKey(internalKey)){
			String reverseKey = "provider:"+providerId+":pid";
			Long pid = getReverseKeyValue(reverseKey);
			if(pid!=null){
				pids.put(providerId, pid);
			}
		}
		
		return pids.get(internalKey);	
	}
	
	public Long setPid(String providerId){
		String internalKey = providerId;
		if(!pids.containsKey(internalKey)){
			pids.put(providerId, getKeyNextValue(PID_KEY));
		}
		
		return pids.get(internalKey);
	}
			
	public Long getSid(String providerId, String sensorId){				
		String internalKey = providerId+"#"+sensorId;
		if(!sids.containsKey(internalKey)){
			String reverseKey = "sensor:"+providerId+":"+sensorId+":sid";
			Long sid = getReverseKeyValue(reverseKey);
			if(sid!=null){
				sids.put(internalKey, sid);
			}
		}				
		
		return sids.get(internalKey);	
	}
		
	public Long setSid(String providerId, String sensorId){
		String internalKey = providerId+"#"+sensorId;
		if(!sids.containsKey(internalKey)){
			sids.put(internalKey, getKeyNextValue(SID_KEY));
		}
		
		return sids.get(internalKey);		
	}
	
	public Long getAid(String alarmId){			
		String internalKey = alarmId;
		if(!aids.containsKey(internalKey)){
			String reverseKey = "alarm:"+alarmId+":aid";
			Long aid = getReverseKeyValue(reverseKey);
			if(aid!=null){
				aids.put(alarmId, aid);
			}
		}
		
		return aids.get(internalKey);	
	}
	
	public Long setAid(String alarmId){
		String internalKey = alarmId;
		if(!aids.containsKey(internalKey)){
			aids.put(alarmId, getKeyNextValue(AID_KEY));
		}
		
		return aids.get(internalKey);
	}
	
	public Long getSdid(){				
		return getKeyNextValue(SDID_KEY);
	}
	
	public Long getSoid(){				
		return getKeyNextValue(SOID_KEY);
	}
	
	public Long getAmid(){				
		return getKeyNextValue(AMID_KEY);
	}
	
	public Long getCurrentSdid(){
		return getCurrentValue(SDID_KEY);
	}
	
	public Long getCurrentSoid(){
		return getCurrentValue(SOID_KEY);
	}
	
	public Long getCurrentAmid(){
		return getCurrentValue(AMID_KEY);
	}
	
	private Long getReverseKeyValue(String reverseKey){								
		String value = jedisTemplate.get(reverseKey);				
		return (NIL.equals(value) || value == null ?null:Long.valueOf(value));		
	}
	
	private Long getKeyNextValue(String key){										
		return jedisTemplate.getKeyNextValue(key);		
	}
	
	private Long getCurrentValue(String key){
		String value = jedisTemplate.get(key);				
		return (NIL.equals(value) || value == null ?new Long(0):Long.valueOf(value));		
	}

	public void setJedisTemplate(JedisTemplate<String, String> jedisTemplate) {
		this.jedisTemplate = jedisTemplate;
	}
		
}
