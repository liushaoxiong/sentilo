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

public class Statistics {
	
	private Devices devices;
	private Performance performance; 
	private Events events;
	private Accounts accounts;
	
	public Statistics(){
		super();
	}
			
	public Statistics(Devices devices, Performance performance, Events events, Accounts accounts) {
		this();
		this.devices = devices;
		this.performance = performance;
		this.events = events;
		this.accounts = accounts;
	}

	public Devices getDevices() {
		return devices;
	}

	public Performance getPerformance() {
		return performance;
	}

	public Events getEvents() {
		return events;
	}
	
	public Accounts getAccounts() {
		return accounts;
	}
		
	public static class Devices{
		private long sensors;
		private long routersAndGateways;
		private long others;				
		
		public Devices(long sensors, long routersAndGateways, long others) {
			super();
			this.sensors = sensors;
			this.routersAndGateways = routersAndGateways;
			this.others = others;
		}
		
		public long getSensors() {
			return sensors;
		}
		public long getRoutersAndGateways() {
			return routersAndGateways;
		}
		public long getOthers() {
			return others;
		}			
	}
	
	public static class Events{
		private long total;
		private long observations;
		private long alarms;	
		private long orders;
		
		public Events(long total, long alarms, long observations, long orders) {
			super();
			this.total = total;
			this.alarms = alarms;
			this.observations = observations;
			this.orders = orders;
		}

		public long getTotal() {
			return total;
		}

		public long getObservations() {
			return observations;
		}

		public long getAlarms() {
			return alarms;
		}

		public long getOrders() {
			return orders;
		}		
	}
	
	public static class Performance{
		private float instantAvg;
		private float dailyAvg;
		private float maxAvg;
		
		public Performance(float instantAvg, float dailyAvg, float maxAvg) {
			super();
			this.instantAvg = instantAvg;
			this.dailyAvg = dailyAvg;
			this.maxAvg = maxAvg;
		}

		public float getInstantAvg() {
			return instantAvg;
		}

		public float getDailyAvg() {
			return dailyAvg;
		}

		public float getMaxAvg() {
			return maxAvg;
		}		
	}
	
	public static class Accounts{
		private long users;
		private long applications;
		private long providers;
		
		public Accounts(long users, long applications, long providers) {
			super();
			this.users = users;
			this.applications = applications;
			this.providers = providers;
		}

		public long getUsers() {
			return users;
		}

		public long getApplications() {
			return applications;
		}

		public long getProviders() {
			return providers;
		}				
		
					
	}

	
}
