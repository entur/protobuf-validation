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

	private static ProtobufValidator globalProtobufValidator = new ProtobufValidator(ValidatorRegistry.globalValidatorRegistry());

	private ValidatorRegistry validatorRegistry;

	/**
	 * @param validatorRegistry The {@link ValidatorRegistry} which should be used for validation.
	 */
	public ProtobufValidator(ValidatorRegistry validatorRegistry) {
		this.validatorRegistry = validatorRegistry;
	}

	private void doValidate(GeneratedMessageV3 message, FieldDescriptor fieldDescriptor, Object fieldValue, DescriptorProtos.FieldOptions options)
			throws IllegalArgumentException, MessageValidationException {
		for (Map.Entry<Descriptors.FieldDescriptor, Object> entry : options.getAllFields().entrySet()) {
			try {
				validatorRegistry.getValidator(entry.getKey()).validate(message, fieldDescriptor, fieldValue, entry);
			} catch(UnsupportedOperationException e) {
				// Add more info and rethrow
				throw new UnsupportedOperationException("Error validating field "+fieldDescriptor+ " with value "+fieldValue+ " and rule "+entry+ " due to "+e.getMessage(),e);
			}
		}
	}

	/**
	 * @param protoMessage The protobuf message object to validate
	 * @throws MessageValidationException Further information about the failed field
	 */
	public void validate(GeneratedMessageV3 protoMessage) throws MessageValidationException {
		for (Descriptors.FieldDescriptor fieldDescriptor : protoMessage.getDescriptorForType().getFields()) {

			Object fieldValue;
			if (fieldDescriptor.isRepeated()) {
				fieldValue = protoMessage.getField(fieldDescriptor);
			} else {
				fieldValue = protoMessage.hasField(fieldDescriptor) ? protoMessage.getField(fieldDescriptor) : null;
			}

			if (protoMessage.getField(fieldDescriptor) instanceof GeneratedMessageV3) {
				doValidate(protoMessage, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
				if (fieldDescriptor.isRepeated() ||protoMessage.hasField(fieldDescriptor)) {
					GeneratedMessageV3 subMessageV3 = (GeneratedMessageV3) protoMessage.getField(fieldDescriptor);
					validate(subMessageV3);
				}
			} else if (fieldDescriptor.getOptions().getAllFields().size() > 0) {
				doValidate(protoMessage, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
			}
		}
	}

	public static ProtobufValidator globalValidator() {
		return globalProtobufValidator;
	}

	public static ProtobufValidator createDefaultValidator() {
		return new ProtobufValidator(ValidatorRegistry.createDefaultRegistry());
	}

}
