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
package org.sentilo.common.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.sentilo.common.utils.DateUtils;


public class DateUtilsTest {
		
	
	@Test
	public void formatAndParseDate(){
		Date currentDate = new Date();
		String formatDate = DateUtils.toStringTimestamp(currentDate);
		assertNotNull(formatDate);
		Date parseDate = DateUtils.stringToDate(formatDate);
		assertNotNull(parseDate);				
		assertEquals(formatDate, DateUtils.toStringTimestamp(parseDate));
	}
	
	@Test
	public void nullStringToDate(){
		assertNull(DateUtils.stringToDate(null));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidStringToDate(){
		DateUtils.stringToDate("12/12/2013");
	}
	
	@Test
	public void nullTimestampToString(){
		assertNull(DateUtils.timestampToString(null));
	}
	
	@Test
	public void timestampToString(){
		Date currentDate = new Date();
		Long currentTimestamp = currentDate.getTime();
		
		String formatDate = DateUtils.timestampToString(currentTimestamp);
		assertNotNull(formatDate);
		assertEquals(formatDate, DateUtils.toStringTimestamp(currentDate));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void invalidTimestampToMillis(){
		DateUtils.toMillis("12/12/2013");
	}
	
	@Test
	public void toMillis(){
		Date currentDate = new Date();
		String formatDate = DateUtils.toStringTimestamp(currentDate);
		assertTrue(DateUtils.toMillis(formatDate)>0);
	}
	
	@Test
	public void parseNullTimestamp(){
		assertNull(DateUtils.parseTimestamp(null));
	}
	
	@Test
	public void parseTimestamp(){
		Date currentDate = new Date();
		String formatDate = DateUtils.toStringTimestamp(currentDate);
		assertTrue(DateUtils.parseTimestamp(formatDate)>0);			
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseInvalidFormatTimestamp(){
		String timestamp = "12/10/2013_23:12:23";
		DateUtils.parseTimestamp(timestamp);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseTimestampWithoutTime(){
		String timestamp = "12/10/2013";
		DateUtils.parseTimestamp(timestamp);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseTransposeTimestamp(){
		String timestamp = "11/23/2013T23:12:23";
		DateUtils.parseTimestamp(timestamp);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void parseTranspose2Timestamp(){
		String timestamp = "2013/11/23T23:12:23";
		DateUtils.parseTimestamp(timestamp);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void toMillisTransposeTimestamp(){
		String timestamp = "11/23/2013T23:12:23";
		DateUtils.toMillis(timestamp);
	}			
}
