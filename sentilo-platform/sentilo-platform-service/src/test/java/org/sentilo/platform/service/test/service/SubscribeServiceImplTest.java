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
package org.sentilo.platform.service.test.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sentilo.common.domain.SubscribeType;
import org.sentilo.platform.common.domain.AlarmSubscription;
import org.sentilo.platform.common.domain.DataSubscription;
import org.sentilo.platform.common.domain.OrderSubscription;
import org.sentilo.platform.common.domain.Subscription;
import org.sentilo.platform.service.dao.JedisTemplate;
import org.sentilo.platform.service.impl.SubscribeServiceImpl;
import org.sentilo.platform.service.utils.ChannelUtils;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.util.CollectionUtils;


public class SubscribeServiceImplTest {	 	
	@Mock private JedisTemplate<String, String> jedisTemplate;
	@Mock private RedisMessageListenerContainer listenerContainer;
	@Mock private DataSubscription dataSubscription; 	
	@Mock private AlarmSubscription alarmSubscription;
	@Mock private OrderSubscription orderSubscription;
	
	private SubscribeServiceImpl service;
	
	@Before
	public void setUp() {						
		MockitoAnnotations.initMocks(this);
		service = new SubscribeServiceImpl();				
		service.setJedisTemplate(jedisTemplate);
		service.setListenerContainer(listenerContainer);										
	}
	
	@Test
	public void dataSubscribe(){		
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
		when(dataSubscription.getSensorId()).thenReturn("sensor1");
		when(dataSubscription.getProviderId()).thenReturn("prov1");
		
		verifySubscribe(dataSubscription);				
	}
	
	@Test
	public void alarmSubscribe(){		
		when(alarmSubscription.getType()).thenReturn(SubscribeType.ALARM);
		when(alarmSubscription.getAlertId()).thenReturn("alarm1");		
		
		verifySubscribe(alarmSubscription);				
	}
	
	@Test
	public void orderSubscribe(){		
		when(orderSubscription.getType()).thenReturn(SubscribeType.ORDER);			
		
		verifySubscribe(orderSubscription);				
	}
	
	@Test
	public void removeSubscriptionWithType(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
		when(dataSubscription.hasResourceIdentified()).thenReturn(true);
		
		Topic topic = ChannelUtils.getChannel(dataSubscription);
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());		
		
		service.remove(dataSubscription);
				
		verify(jedisTemplate).hDel(key, topic.getTopic());
						
	}
	
	@Test
	public void removeSubscriptionOfOneType(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
		when(dataSubscription.hasResourceIdentified()).thenReturn(false);
						
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());		
		Set<String> topics = getTopics();
		List<String> topicsToRemove = ChannelUtils.filterTopicsOfType(topics, dataSubscription.getType());
		
		when(jedisTemplate.hKeys(key)).thenReturn(topics);
		
		service.remove(dataSubscription);
				
		verify(jedisTemplate).hKeys(key);
		verify(jedisTemplate).hDel(key, topicsToRemove.toArray(new String[0]));
						
	}
	
	@Test
	public void removeSubscriptionOfOneTypeWithoutSubscriptions(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
		when(dataSubscription.hasResourceIdentified()).thenReturn(false);
						
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());		
		Set<String> topics = getEmptyTopics();
		List<String> topicsToRemove = ChannelUtils.filterTopicsOfType(topics, dataSubscription.getType());
		
		when(jedisTemplate.hKeys(key)).thenReturn(topics);
		
		service.remove(dataSubscription);
				
		verify(jedisTemplate).hKeys(key);
		verify(jedisTemplate, times(0)).hDel(key, topicsToRemove.toArray(new String[0]));
						
	}
	
	@Test
	public void removeSubscriptionWithoutType(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(null);
		when(dataSubscription.hasResourceIdentified()).thenReturn(true);
				
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());		
		
		service.remove(dataSubscription);
				
		verify(jedisTemplate).hKeys(key);
		verify(jedisTemplate).del(key);
						
	}
	
	@Test
	public void getSubscriptions(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
					
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());		
		when(jedisTemplate.hGetAll(key)).thenReturn(buildSubscriptions());
		
		List<Subscription> subscriptions = service.get(dataSubscription);
				
		verify(jedisTemplate).hGetAll(key);
		assertTrue(subscriptions.size() == buildSubscriptions().size());
						
	}
	
	@Test
	public void getSubscriptionsWithoutSubscriptions(){		
		initSubscription(dataSubscription);
		when(dataSubscription.getType()).thenReturn(SubscribeType.DATA);
					
		String key = service.getKeysBuilder().getSubscriptionKey(dataSubscription.getSourceEntityId());						
		List<Subscription> subscriptions = service.get(dataSubscription);
				
		verify(jedisTemplate).hGetAll(key);
		assertTrue(CollectionUtils.isEmpty(subscriptions));
						
	}
	
	private void initSubscription(Subscription subscription){
		String entityId = "prov1";
		String endpoint = "http://www.opentrends.net/endpoint";
		when(subscription.getSourceEntityId()).thenReturn(entityId);
		when(subscription.getEndpoint()).thenReturn(endpoint);
	}
	
	private void verifySubscribe(Subscription subscription){
		initSubscription(subscription);
		Topic topic = ChannelUtils.getChannel(subscription);
		String key = service.getKeysBuilder().getSubscriptionKey(subscription.getSourceEntityId());		
		
		service.subscribe(subscription);
				
		verify(jedisTemplate).hSet(key, topic.getTopic(), subscription.getEndpoint());
	}
	
	private Set<String> getTopics(){
		Set<String> topics = new HashSet<String>();
		topics.add("data:provider1:sensor1");
		topics.add("data:provider2*");				
		return topics;
	}
	
	private Set<String> getEmptyTopics(){
		return new HashSet<String>();								
	}
	
	private Map<String,String> buildSubscriptions(){
		Map<String,String> subscriptions = new HashMap<String,String>();
		subscriptions.put("data:provider1:sensor1", "endpoint1");
		subscriptions.put("data:provider2*", "endpoint2");				
		return subscriptions;
	}		
}
