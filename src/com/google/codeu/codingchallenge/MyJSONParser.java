// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.io.IOException;

final class MyJSONParser implements JSONParser {

	@Override
	public JSON parse(String in) throws IOException {
		MyJSON toBeReturned = new MyJSON();
		for (int index = 0; index < in.length(); index++) {
			char c = in.charAt(index);
			// skip these characters
			if (c == '{' || c == '}' || c == ',' || c == '\n' || c == '\t'
					|| c == ' ') {
				continue;
			}
			String key = "";
			// if this is the beginning of a name
			if (c == '"') {
				c = in.charAt(++index);
				while (c != '"') {
					key += c;
					c = in.charAt(++index);
				}
			}
			c = in.charAt(++index);
			// skip these characters
			while (c == ',' || c == '\n' || c == '\t' || c == ' ') {
				c = in.charAt(++index);
			}
			// ensure that parsing is going smoothly and that a colon is next
			if (c != ':') {
				return null;
			}
			c = in.charAt(++index);
			// skip these characters
			while (c == ',' || c == '\n' || c == '\t' || c == ' ') {
				c = in.charAt(++index);
			}
			// Decide what kind of value is present
			if (c == '{') {
				String nestedJSON = "{";
				int expectedClosingBraces = 1;
				while (expectedClosingBraces > 0) {
					c = in.charAt(++index);
					if (c == '{')
						expectedClosingBraces++;
					else if (c == '}')
						expectedClosingBraces--;
					nestedJSON += c;
				}
				toBeReturned.setObject(key, parse(nestedJSON));
			} else if (c == '"') {
				String stringValue = "";
				c = in.charAt(++index);
				while (c != '"') {
					stringValue += c;
					c = in.charAt(++index);
				}
				toBeReturned.setString(key, stringValue);
			}
		}
		return toBeReturned;
	}
}
