/**
 * Test_InterviewUtils.java
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
 */
package com.commercedirect.interview;

import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rmitchell
 * @version 1.0
 * @since 2015-12-01
 *
 */
public class Test_InterviewUtils {
	private Map<String, Integer>	mapExpected;
	private Path					tempFile;
	

	@Before
	public void before() {
		// initialize our pseudo csv-like data
		List<String> rows = new ArrayList<String>();
		rows.add("John,2");
		rows.add("Jane,3");
		rows.add("John,4");
		rows.add("Jane,5");
		rows.add("John,10"); // extra data for John
		rows.add("Jane,20"); // extra data for Jane
		
		// initialize our expected map of pseudo name,count pairs
		this.mapExpected = new HashMap<String, Integer>();
		this.mapExpected.put("John", 16);
		this.mapExpected.put("Jane", 28);

		// populate csv-like temp file
		try {
			this.tempFile = Files.createTempFile("name_count_pairs", ".csv");
			Files.write(tempFile, rows, Charset.defaultCharset(), StandardOpenOption.WRITE);
			System.out.println("temp file: " + tempFile.toString());
		} catch (Exception e) {
			System.err.println("Exception populating temp file: " + e.getMessage());
			System.exit(-1);
		}
	}
	
	@After
	public void after() {
		try {
			Files.deleteIfExists(this.tempFile);
		} catch (Exception e) {
			// ignore
		}
	}
	
	/**
	 * This test uses the prebuilt temp file containing our test data. We simply use the temp file
	 * to pass to our test method. We then compare its returned values to our expected values.
	 * 
	 */
	@Test
	public void test_CountPairs() {
		Map<String, Integer> map = InterviewUtils.countPairs(this.tempFile.toFile().getAbsolutePath());
		assertTrue("Fail, the returned map is empty", !map.isEmpty());
		assertTrue("Fail, the returned map contents does not match expected", map.entrySet().containsAll(this.mapExpected.entrySet()));

		// per the requirement, display the returned map contents
		for (String key : map.keySet()) {
			String msg = String.format("The total for %s is %d", key, map.get(key));
			System.out.println(msg);
		}
	}
	
	@Test
	public void test_safeToInt() {
		assertTrue("Fail, expected \"5\"==5", InterviewUtils.safeToInt("5", 0) == 5);
		assertTrue("Fail, expected \"-5\"==-5", InterviewUtils.safeToInt("-5", 0) == -5);
		assertTrue("Fail, expected \"a\"==0", InterviewUtils.safeToInt("a", 0) == 0);
		assertTrue("Fail, expected null==-1", InterviewUtils.safeToInt(null, -1) == -1);
	}
	
	@Test
	public void test_isPalindrome() {
		Object[][] data = new Object[][] {
			{Boolean.TRUE, "pop"},
			{Boolean.TRUE, "Ah, Satan sees Natasha"},
			{Boolean.FALSE, "definitely not a Palindrome"},
			{Boolean.TRUE, "A nut for a jar of tuna"},
			{Boolean.TRUE, "A lad named E. Mandala"}
		};
		
		for (Object[] d : data) {
			Boolean expect = (Boolean)d[0];
			String tmp = (String)d[1];
			Boolean result = InterviewUtils.isPalindrome(tmp);
			Boolean pass = (expect.equals(result));
			
			String msg = String.format("%s Palindrome test: expect(%5b) result(%5b) \"%s\" ", 
				(pass?"Pass":"Fail"), expect, result, tmp);
			System.out.println(msg);
			
			assertTrue("Fail, expect("+expect+"), result("+result+") for test(\""+tmp+"\")", pass);
		}
	}
}
