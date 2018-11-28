
package no.entur.protobuf.validation.validators;

/*-
 * #%L
 * Protobuf validator
 * %%
 * Copyright (C) 2017 - 2018 Original authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;

import no.entur.protobuf.validation.MessageValidationException;
import no.entur.protobuf.validation.ValidationConditions;
import no.entur.protobuf.validation.Validator;

import com.google.protobuf.GeneratedMessageV3;

/**
 *
 * @author Serious
 * @author seime
 * @date 2017/6/28
 */
public class RegexValidator implements Validator {
	
	private static final Map<String,Pattern> patternCache = new ConcurrentHashMap<>();
	
	@Override
	public void validate(GeneratedMessageV3 protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue, Map.Entry<Descriptors.FieldDescriptor, Object> rule)
			throws MessageValidationException {
		
		String regex = rule.getValue().toString();
		
		Pattern pattern = patternCache.get(regex);
		if(pattern == null) {
			pattern = Pattern.compile(regex);
			patternCache.put(regex, pattern);
		}
		
		String fieldValueStr = fieldValue.toString();
		ValidationConditions.checkRule(pattern.matcher(fieldValueStr).matches(), protoMessage, fieldDescriptor, fieldValue, rule);
	}
}
