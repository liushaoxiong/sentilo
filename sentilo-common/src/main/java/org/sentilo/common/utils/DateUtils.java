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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public abstract class DateUtils {

	private static final String TIMESTAMP_PATTERN = "dd/MM/yyyy'T'HH:mm:ss";
	private static final DateFormat PSAB_DF;
	
	static{
		PSAB_DF = new SimpleDateFormat(TIMESTAMP_PATTERN);
		PSAB_DF.setLenient(false);
	}

	private DateUtils(){
		throw new AssertionError();
	}
	
	public static String toStringTimestamp(Date date) {		
		return PSAB_DF.format(date);
	}

	public static long toMillis(String timestamp) {
		return stringToDate(timestamp).getTime();
	}

	public static Date stringToDate(String date) {
		try {					
			return (StringUtils.hasText(date)?PSAB_DF.parse(date):null);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Error parsing date", e);
		}
	}
	
	public static Long parseTimestamp(String timestamp){
		Date date = stringToDate(timestamp);
		return (date!=null?date.getTime():null);
	}
	
	public static String timestampToString(Long timestamp){				
		return (timestamp==null?null:PSAB_DF.format(timestamp));
	}
	
	
}
