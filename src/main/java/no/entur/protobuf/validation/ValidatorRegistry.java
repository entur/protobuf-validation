
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
	private static final Map<Descriptors.FieldDescriptor, Validator> REGISTRY = Maps.newHashMap();

	static {
		REGISTRY.put(Validation.max.getDescriptor(), new MaxValidator());
		REGISTRY.put(Validation.min.getDescriptor(), new MinValidator());
		REGISTRY.put(Validation.repeatMax.getDescriptor(), new RepeatMaxValidator());
		REGISTRY.put(Validation.repeatMin.getDescriptor(), new RepeatMinValidator());
		REGISTRY.put(Validation.future.getDescriptor(), new FutureValidator());
		REGISTRY.put(Validation.past.getDescriptor(), new PastValidator());
		REGISTRY.put(Validation.regex.getDescriptor(), new RegexValidator());
		REGISTRY.put(Validation.required.getDescriptor(), new RequiredValidator());
		REGISTRY.put(Validation.forbidden.getDescriptor(), new ForbiddenValidator());
	}

	public static Validator getValidator(Descriptors.FieldDescriptor descriptor) {
		return REGISTRY.get(descriptor);
	}
}
