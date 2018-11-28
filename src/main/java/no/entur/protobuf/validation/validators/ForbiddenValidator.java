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

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;

import no.entur.protobuf.validation.MessageValidationException;
import no.entur.protobuf.validation.ValidationConditions;
import no.entur.protobuf.validation.Validator;

import com.google.protobuf.GeneratedMessageV3;

public class ForbiddenValidator implements Validator {
	@Override
	public void validate(GeneratedMessageV3 messageV3, FieldDescriptor fieldDescriptor, Object fieldValue, Map.Entry<Descriptors.FieldDescriptor, Object> rule)
			throws MessageValidationException {
		ValidationConditions.checkRule(!messageV3.hasField(fieldDescriptor), messageV3, fieldDescriptor, fieldValue, rule);
	}

}
