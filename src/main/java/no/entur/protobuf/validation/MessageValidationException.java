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

public class MessageValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	private static String computeMessage(GeneratedMessageV3 message, FieldDescriptor fieldDescriptor, Object fieldValue,
			Map.Entry<Descriptors.FieldDescriptor, Object> validationRule) {
		StringBuilder b = new StringBuilder();
		b.append(fieldDescriptor.getFile().getName());
		b.append('/');
		b.append(fieldDescriptor.getFullName());
		b.append(", rule ");
		String validationRuleString;
		if (validationRule.getValue() == null || "".equals(validationRule.getValue())) {
			validationRuleString = validationRule.getKey().toString();
		} else {
			validationRuleString = validationRule.toString();
		}

		b.append(validationRuleString);
		if (null != fieldValue && !"".equals(fieldValue)) {
			b.append(", invalid field value is ");
			b.append(fieldValue);
		}
		return b.toString();
	}

	private String fieldName;

	private Object fieldValue;

	private String protoName;

	private Object validationRule;

	public MessageValidationException(GeneratedMessageV3 message, FieldDescriptor fieldDescriptor, Object fieldValue,
			Map.Entry<Descriptors.FieldDescriptor, Object> validationRule) {
		super(computeMessage(message, fieldDescriptor, fieldValue, validationRule));
		this.protoName = fieldDescriptor.getFile().getName();
		this.fieldName = fieldDescriptor.getFullName();
		this.fieldValue = fieldValue;
		this.validationRule = validationRule;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public String getProtoName() {
		return protoName;
	}

	public Object getValidationRule() {
		return validationRule;
	}

}
