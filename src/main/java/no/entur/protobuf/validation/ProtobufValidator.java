package no.entur.protobuf.validation;

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
import java.util.Map;

/**
 *
 * @author Serious
 * @author seime
 */
public class ProtobufValidator {

	private static ProtobufValidator globalProtobufValidator = new ProtobufValidator();

	private ValidatorRegistry validatorRegistry;

	/**
	 * @param validatorRegistry The {@link ValidatorRegistry} which should be used for validation.
	 */
	public ProtobufValidator(ValidatorRegistry validatorRegistry) {
		this.validatorRegistry = validatorRegistry;
	}

    /**
     * The default constructor which uses the global {@link ValidatorRegistry}.
     */
	public ProtobufValidator() {
	    this(ValidatorRegistry.globalValidatorRegistry());
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
                //validate array of messages recursively
			} else {
				fieldValue = protoMessage.hasField(fieldDescriptor) ? protoMessage.getField(fieldDescriptor) : null;
                //validate recursively the message
                if (fieldValue instanceof GeneratedMessageV3)
                    validate((GeneratedMessageV3) fieldValue);
			}

            //validate options on the field
            if (fieldDescriptor.getOptions().getAllFields().size() > 0) {
                doValidate(protoMessage, fieldDescriptor, fieldValue, fieldDescriptor.getOptions());
            }
		}
	}

    /**
     * @return The globally shared {@link ProtobufValidator}.
     */
	public static ProtobufValidator globalValidator() {
		return globalProtobufValidator;
	}

    /**
     * @return A {@link ProtobufValidator} with a default {@link ValidatorRegistry}.
     */
	public static ProtobufValidator createDefaultValidator() {
		return new ProtobufValidator(ValidatorRegistry.createDefaultRegistry());
	}

}
