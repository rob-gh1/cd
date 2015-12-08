/**
 * InterviewUtils.java
 * 
 * Copyright (c) 2015 Rob Mitchell. All Right Reserved.
 *  
 * This software is the confidential and proprietary information of
 * Rob Mitchell ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with Rob Mitchell.
 *
 * Rob Mitchell MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 * A collection of utility methods for the Commerce Direct interview. We 
 * define these statically for ease of calling since they're just utility
 * methods.
 * 
 * @author rmitchell
 * @version 1.0
 * @since 2015-12-01
 */

package com.commercedirect.interview;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rmitchell
 * @version 1.0
 * @since 2015-12-01
 *
 */
public class InterviewUtils {

	/**
	 * Sums-up the names in a text file. The file contains data in the format of "name,count"
	 * where name is a string and count is a number. Each line will only contain one name-count 
	 * pair. 
	 * 
	 * Return should contain each name occurring in the file along with the sum of the count for 
	 * all occurrences of a given name.
	 *  
	 * @param file - absolute location of a text file
	 * @return map of name to count values (can be empty)
	 */
	public static Map<String, Integer> countPairs(String file) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		// try with resources and uses closables
		try (
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis)); ) {

			String line = null;
			while ((line = reader.readLine()) != null) {
				NameCount nv = new NameCount(line);
				Integer tmp = map.get(nv.name);
				map.put(nv.name, nv.count + ((tmp == null) ? 0 : tmp));
			}
		} catch (IOException e) {
			// ignore
		}		
		return map;
	}

	/**
	 * Helper class for parsing csv-like string lines into name,count pairs. We
	 * silently ignore any exceptions in an attempt to get to the desiredd answer. 
	 * Future versions may want to signal if an exception occurs. 
	 * 
	 * @author rmitchell
	 * @version 1.0
	 * @since 2015-12-01
	 *
	 */
	private static final class NameCount {
		String	name;
		int		count;
		
		private NameCount(String line) {
			try {
				String[] row = line.split(",");
				this.name = row[0];
				this.count = InterviewUtils.safeToInt(row[1], 0);
			} catch (Exception e) {
				// ignore
			}
		}
	}
	
	/**
	 * Safely convert a string numeric to a primitive int value.
	 * 
	 * @param val
	 * @param defaultVal
	 * @return convert the string numeric to a primitive int; any exception, then return defaultVal
	 */
	public static int safeToInt(String val, int defaultVal) {
		int tmp = defaultVal;
		try {
			tmp = Integer.valueOf(val);
		} catch (Exception e) {
			// ignore
		}
		return tmp;
	}
	
	/**
	 * Checks if a given string parameter is a Palindrome e.g. its reverse match the original.
	 * We can ignore all non-alphanumeric characters and assume that upper-and lower-case 
	 * characters are identical 
	 * 
	 * @param value - the parameter to verify
	 * @return true if a the parameter is a Palindrome; otherwise false
	 */
	public static Boolean isPalindrome(String value) {
		// quick sanity check
		if (value == null) {
			return Boolean.FALSE;
		}
		
		// simply remove all non-alphanumeric characters. Must have something to work with.
		final String tmp1 = value.replaceAll("[^a-zA-Z0-9]", "");
		if (tmp1.length() < 2) {
			return Boolean.FALSE;
		}
		
		// reverse and compare case insensitive
		final String tmp2 = new StringBuilder(tmp1).reverse().toString();
		return tmp2.equalsIgnoreCase(tmp1);
	}
}
