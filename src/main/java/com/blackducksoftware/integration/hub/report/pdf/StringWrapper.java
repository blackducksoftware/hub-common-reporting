/**
 * Hub Common Reporting
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.blackducksoftware.integration.hub.report.pdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StringWrapper {

    public static final int DEFAULT_CHAR_LENGTH = 20;

    public static String wrap(final String str, final int charLimit) {
        final ArrayList<String> subWords = new ArrayList<>(Arrays.asList(str.split(" ")));
        for (int i = 0; i < subWords.size(); i++) {
            final String currentSubWord = subWords.get(i);
            if (subWords.get(i).length() > charLimit) {
                subWords.remove(currentSubWord);
                subWords.addAll(i, modifyWrapString(currentSubWord, charLimit));
            }
        }
        return StringUtils.joinWith(" ", subWords);
    }

    public static String wrap(final String str) {
        final ArrayList<String> subWords = new ArrayList<>(Arrays.asList(str.split(" ")));
        for (int i = 0; i < subWords.size(); i++) {
            final String currentWord = subWords.get(i);
            if (currentWord.length() > DEFAULT_CHAR_LENGTH) {
                subWords.remove(currentWord);
                subWords.addAll(i, modifyWrapString(currentWord, DEFAULT_CHAR_LENGTH));
            }
        }
        return StringUtils.joinWith(" ", subWords);
    }

    public static List<String> modifyWrapString(final String str, final int charLimit) {
        int lastBreak = 0;
        int maxLengthCounter = 0;
        final int strLen = str.length();
        final ArrayList<String> brokenUpStrings = new ArrayList<>();
        for (int i = 0; i < strLen; i++) {
            if (!StringUtils.isAlphanumeric(str.charAt(i) + "") || maxLengthCounter == charLimit) {
                brokenUpStrings.add(str.substring(lastBreak, i));
                lastBreak = i;
                maxLengthCounter = 0;
            } else {
                maxLengthCounter++;
            }

            if (brokenUpStrings.size() > 0 && strLen - i < charLimit) {
                brokenUpStrings.add(str.substring(i, strLen));
                break;
            }
        }
        return brokenUpStrings;
    }
}
