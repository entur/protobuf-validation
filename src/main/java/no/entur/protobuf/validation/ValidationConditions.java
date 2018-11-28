package no.entur.protobuf.validation;

import java.util.Map;

import com.google.protobuf.Descriptors;

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

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessageV3;

public class ValidationConditions {
	public static void checkRule(boolean b, GeneratedMessageV3 protoMessage, FieldDescriptor fieldDescriptor, Object fieldValue,
			Map.Entry<Descriptors.FieldDescriptor, Object> extensionValue) throws MessageValidationException {
		if (!b) {
			throw new MessageValidationException(protoMessage, fieldDescriptor, fieldValue, extensionValue);
		}
	}
}
