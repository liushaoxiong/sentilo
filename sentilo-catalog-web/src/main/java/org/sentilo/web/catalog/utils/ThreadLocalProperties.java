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
package org.sentilo.web.catalog.utils;

/**
 * Thread local que nos proporciona un backend en el cual mantener el nombre del ds a utilizar a la hora de persistir la info en la Situation Room.
 * Este backend es utilizado en la clase {@link SentiloRoutingDataSource} para poder hacer el lookup del ds correspondiente.
 * @see SentiloRoutingDataSource 
 */
public abstract class ThreadLocalProperties {
	
	public static final ThreadLocal<String> DS_THREAD_LOCAL = new ThreadLocal<String>();
	
	public static void set(String dsName) {
		DS_THREAD_LOCAL.set(dsName);
    }

    public static void unset() {
    	DS_THREAD_LOCAL.remove();
    }

    public static String get() {
        return DS_THREAD_LOCAL.get();
    }
    
    private ThreadLocalProperties(){
		//this prevents even the native class from calling this ctor as well :
	    throw new AssertionError();
	}

}
