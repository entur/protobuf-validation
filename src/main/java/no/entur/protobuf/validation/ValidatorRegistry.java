
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

import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;

import no.entur.protobuf.validation.validators.ForbiddenValidator;
import no.entur.protobuf.validation.validators.FutureValidator;
import no.entur.protobuf.validation.validators.MaxValidator;
import no.entur.protobuf.validation.validators.MinValidator;
import no.entur.protobuf.validation.validators.PastValidator;
import no.entur.protobuf.validation.validators.RegexValidator;
import no.entur.protobuf.validation.validators.RepeatMaxValidator;
import no.entur.protobuf.validation.validators.RepeatMinValidator;
import no.entur.protobuf.validation.validators.RequiredValidator;
import validation.Validation;

/**
 * @author Serious
 * @author seime
 */
public class ValidatorRegistry {

	private static ValidatorRegistry globalValidatorRegistry = createDefaultRegistry();

	private Map<Descriptors.FieldDescriptor, Validator> validatorMap;

	public ValidatorRegistry(Map<Descriptors.FieldDescriptor, Validator> validatorMap) {
		this.validatorMap = validatorMap;
	}

	public ValidatorRegistry() {
		this(Maps.newConcurrentMap());
	}

	public Validator getValidator(Descriptors.FieldDescriptor descriptor) {
		return validatorMap.get(descriptor);
	}

	/**
	 * Adds a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}.
	 * If there is already a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}
	 * the previous {@link Validator} will be overridden.
	 *
	 * @param fieldDescriptor The {@link com.google.protobuf.Descriptors.FieldDescriptor} to add
	 * @param validator The {@link Validator} to add in context with the fieldDescriptor
	 */
	public void addValidator(Descriptors.FieldDescriptor fieldDescriptor, Validator validator) {
		validatorMap.put(fieldDescriptor, validator);
	}

	/**
	 * Adds multiple {@link Validator}s in context to their {@link com.google.protobuf.Descriptors.FieldDescriptor}.
	 * If there is already a {@link Validator} for a {@link com.google.protobuf.Descriptors.FieldDescriptor}
	 * the previous {@link Validator} will be overridden.
	 *
	 * @param validators The {@link com.google.protobuf.Descriptors.FieldDescriptor} to add
	 */
	public void addValidators(Map<Descriptors.FieldDescriptor, Validator> validators) {
		validatorMap.putAll(validators);
	}

	/**
	 * @param fieldDescriptor The {@link com.google.protobuf.Descriptors.FieldDescriptor}
	 *                        for which the {@link Validator} should be removed.
	 */
	public void removeValidator(Descriptors.FieldDescriptor fieldDescriptor) {
		validatorMap.remove(fieldDescriptor);
	}

	/**
	 * @param fieldDescriptors The {@link com.google.protobuf.Descriptors.FieldDescriptor}s
	 *                        for which their {@link Validator}s should be removed.
	 */
	public void removeValidator(Iterable<Descriptors.FieldDescriptor> fieldDescriptors) {
		for (Descriptors.FieldDescriptor fieldDescriptor : fieldDescriptors) {
			validatorMap.remove(fieldDescriptor);
		}
	}


	public static ValidatorRegistry globalValidatorRegistry() {
		return globalValidatorRegistry;
	}

	/**
	 * @return A {@link ValidatorRegistry} with all built-in validators.
	 */
	public static ValidatorRegistry createDefaultRegistry() {

		ValidatorRegistry validatorRegistry = new ValidatorRegistry();

		Map<Descriptors.FieldDescriptor, Validator> validatorMap = validatorRegistry.validatorMap;
		validatorMap.put(Validation.max.getDescriptor(), new MaxValidator());
		validatorMap.put(Validation.min.getDescriptor(), new MinValidator());
		validatorMap.put(Validation.repeatMax.getDescriptor(), new RepeatMaxValidator());
		validatorMap.put(Validation.repeatMin.getDescriptor(), new RepeatMinValidator());
		validatorMap.put(Validation.future.getDescriptor(), new FutureValidator());
		validatorMap.put(Validation.past.getDescriptor(), new PastValidator());
		validatorMap.put(Validation.regex.getDescriptor(), new RegexValidator());
		validatorMap.put(Validation.required.getDescriptor(), new RequiredValidator());
		validatorMap.put(Validation.forbidden.getDescriptor(), new ForbiddenValidator());

		return validatorRegistry;
	}
}
