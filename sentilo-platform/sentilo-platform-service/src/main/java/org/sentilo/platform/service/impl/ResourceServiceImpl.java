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
package org.sentilo.platform.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sentilo.platform.common.domain.Sensor;
import org.sentilo.platform.common.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


@Service
public class ResourceServiceImpl extends AbstractPlatformServiceImpl implements ResourceService {

private final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);	
			
	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#registerProviderIfNecessary(java.lang.String)
	 */
	public Long registerProviderIfNecessary(String providerId){				
		Long pid = jedisSequenceUtils.getPid(providerId);
		if(pid == null){ //se debe registrar el proveedor
			pid = jedisSequenceUtils.setPid(providerId);			
			jedisTemplate.set(keysBuilder.getProviderKey(pid), providerId);
			// Y definimos una reverse lookup key con la cual recuperar rapidamente el pid del proveedor providerId
			jedisTemplate.set(keysBuilder.getReverseProviderKey(providerId), pid.toString());				
			logger.debug("Registered in Redis provider {} with pid {}", providerId, pid);
		}															
		
		return pid;								
	}

	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#registerSensorIfNecessary(java.lang.String, java.lang.String)
	 */
	public Long registerSensorIfNecessary(String sensorId, String providerId){				
		Long sid = jedisSequenceUtils.getSid(providerId, sensorId);
		if(sid == null){ //se debe registrar el sensor
			Long pid = jedisSequenceUtils.getPid(providerId);
			sid = jedisSequenceUtils.setSid(providerId, sensorId);
			
			//Guardamos una hash de clave sid:{sid} y valores provider y sensor
			Map<String,String> fields = new HashMap<String, String>();
			fields.put(PROVIDER, providerId);
			fields.put(SENSOR, sensorId);
			jedisTemplate.hmSet(keysBuilder.getSensorKey(sid), fields);
			
			// Definimos una reverse lookup key con la cual recuperar rapidamente los sensores de un proveedor			
			jedisTemplate.sAdd(keysBuilder.getProviderSensorsKey(pid), sid.toString());
			
			// Y por ultimo añadimos una ultima reverse lookup key que nos permita recuperar rapidamente el sid de un sensor
			// sabiendo el proveedor y el sensor, es decir, poder almacenar rapidamente el dato /provider1/sensor2/26
			jedisTemplate.set(keysBuilder.getReverseSensorKey(providerId, sensorId), sid.toString());
			
			logger.debug("Registered in Redis sensor {} from provider {} with sid {}", sensorId, providerId, sid);
		}
														
		return sid;				
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#getSensorsFromProvider(java.lang.String)
	 */
	public Set<String> getSensorsFromProvider(String providerId){
		Long pid = jedisSequenceUtils.getPid(providerId);
		if(pid == null){
			// Si no hay identificador interno del proveedor, entonces este no esta registrado en Redis y por lo tanto
			// no tiene ningun sensor asociado
			return null;
		}
								
		return jedisTemplate.sMembers(keysBuilder.getProviderSensorsKey(pid));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#getSensorsToInspect(java.lang.String, java.lang.String)
	 */
	public Set<String> getSensorsToInspect(String providerId, String sensorId){		
		Set<String> sids = null;
		
		if(!StringUtils.hasText(providerId)){
			return sids;
		}
		
		if(StringUtils.hasText(sensorId)){
			Long sid = jedisSequenceUtils.getSid(providerId, sensorId);
			if(sid != null){
				sids = new HashSet<String>();
				sids.add(sid.toString());
			}
		}else{
			sids = getSensorsFromProvider(providerId);
		}
		
		return sids;		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#getSensor(java.lang.Long)
	 */
	public Sensor getSensor(Long sid){		
		Sensor sensor = null;											
		Map<String, String> infoSid = jedisTemplate.hGetAll(keysBuilder.getSensorKey(sid));
		if(!CollectionUtils.isEmpty(infoSid)){
			String sensorId = infoSid.get(SENSOR);
			String providerId = infoSid.get(PROVIDER);
			sensor = new Sensor(sensorId, providerId);
		}
		return sensor;													
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.sentilo.platform.common.service.ResourceService#registerAlarmIfNecessary(java.lang.String)
	 */
	public Long registerAlarmIfNecessary(String alarmId){
		Long aid = jedisSequenceUtils.getAid(alarmId);
		if(aid == null){ //se debe registrar la alarma
			aid = jedisSequenceUtils.setAid(alarmId);			
			jedisTemplate.set(keysBuilder.getAlarmKey(aid), alarmId);
			// Definimos una reverse lookup key con la cual recuperar rapidamente el aid de la alarma alarmId
			jedisTemplate.set(keysBuilder.getReverseAlarmKey(alarmId), aid.toString());				
			logger.debug("Registered in Redis alarm {} with aid {}", alarmId, aid);
		}															
		
		return aid;	
	}

}
