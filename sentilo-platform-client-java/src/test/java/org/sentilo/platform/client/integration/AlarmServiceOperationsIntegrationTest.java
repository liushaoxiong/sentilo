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
package org.sentilo.platform.client.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.sentilo.common.domain.QueryFilterParams;
import org.sentilo.platform.client.core.PlatformClientOperations;
import org.sentilo.platform.client.core.domain.AlarmInputMessage;
import org.sentilo.platform.client.core.domain.AlarmsOutputMessage;
import org.sentilo.platform.client.core.exception.PlatformClientAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/sentilo-platform-client-integration.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AlarmServiceOperationsIntegrationTest {
	
	final String ALERT_NAME = "testAlert";
	
	@Autowired
	protected PlatformClientOperations platformTemplate;	
	
	
	@Test
	public void _01_publish() throws Exception{
		AlarmInputMessage message = new AlarmInputMessage(ALERT_NAME, "umbral superado");
		platformTemplate.getAlarmOps().publish(message);			
		assertTrue("No se ha realizado correctamente la llamada a la plataforma",true);
	}
	
	@Test
	public void _02_publishWithoutAlarmMessage() throws Exception{
		AlarmInputMessage message = new AlarmInputMessage("alerta1");
		boolean error = false;
		try {
			platformTemplate.getAlarmOps().publish(message);
		} catch (PlatformClientAccessException e) {
			error = true;
		}			
		assertTrue("No se ha realizado correctamente la llamada a la plataforma",error);
	}
	
	@Test
	public void _03_getLastAlarmMessages() throws Exception{
		AlarmInputMessage message = new AlarmInputMessage(ALERT_NAME);		
		AlarmsOutputMessage response = platformTemplate.getAlarmOps().getLastAlarmMessages(message);					
		assertTrue(response!=null && !CollectionUtils.isEmpty(response.getMessages()));
		assertTrue(response.getMessages().size() == 1);
	}
	
	@Test
	public void _04_getFilteredLastAlarmMessages() throws Exception{
		QueryFilterParams filterParams = new QueryFilterParams(2);
		AlarmInputMessage message = new AlarmInputMessage(ALERT_NAME, filterParams);
		
		AlarmsOutputMessage response = platformTemplate.getAlarmOps().getLastAlarmMessages(message);					
		assertTrue(response!=null && !CollectionUtils.isEmpty(response.getMessages()));
		assertEquals(2, response.getMessages().size());
	}
	
}
