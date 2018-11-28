package no.entur.protobuf.validation;

import java.util.Map;

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

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.GeneratedMessageV3;

/**
 *
 * @author Serious
 * @author seime
 */
public class ProtobufValidator {
	private void doValidate(GeneratedMessageV3 message, FieldDescriptor fieldDescriptor, Object fieldValue, DescriptorProtos.FieldOptions options)
			throws IllegalArgumentException, MessageValidationException {
		for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : options.getAllFields().entrySet()) {
			ValidatorRegistry.getValidator(entry.getKey()).validate(message, fieldDescriptor, fieldValue, entry);
		}
	}

	public void validate(GeneratedMessageV3 messageV3) throws MessageValidationException {
		for (Descriptors.FieldDescriptor fieldDescriptor : messageV3.getDescriptorForType().getFields()) {

			Object fieldValue = null;
			if (fieldDescriptor.isRepeated()) {
				fieldValue = messageV3.getField(fieldDescriptor);
			} else {
				fieldValue = messageV3.hasField(fieldDescriptor) ? messageV3.getField(fieldDescriptor) : null;
			}

			if (messageV3.getField(fieldDescriptor) instanceof GeneratedMessageV3) {
				doValidate(messageV3, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
				if (messageV3.hasField(fieldDescriptor)) {
					GeneratedMessageV3 subMessageV3 = (GeneratedMessageV3) messageV3.getField(fieldDescriptor);
					validate(subMessageV3);
				}
			} else if (fieldDescriptor.getOptions().getAllFields().size() > 0) {
				doValidate(messageV3, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
			}
		}
	}

}
