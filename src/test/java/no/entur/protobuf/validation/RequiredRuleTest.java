package no.entur.protobuf.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import validation.Required;
import validation.RequiredRepeated;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

public class RequiredRuleTest {

	private ProtobufValidator validator = new ProtobufValidator();

	@Test
	public void testRuleFail() {
		Required b = Required.newBuilder().build();
		Executable e = () -> validator.validate(b);
		assertThrows(MessageValidationException.class, e);

	}

	@Test
	public void testRulePass() {
		Required b = Required.newBuilder().setName("Required").build();

		Executable e = () -> validator.validate(b);
		assertDoesNotThrow(e);
	}

	@Test
	public void testEmptyString() {
		Required b = Required.newBuilder().setName("").build();

		Executable e = () -> validator.validate(b);
		assertThrows(MessageValidationException.class, e);
	}

	@Test
	public void testRepeatedFieldPass() {
		RequiredRepeated b = RequiredRepeated.newBuilder().addName("").build();

		Executable e = () -> validator.validate(b);
		assertDoesNotThrow(e);
	}

	@Test
	public void testRepeatedFieldFail() {
		RequiredRepeated b = RequiredRepeated.newBuilder().build();
		Executable e = () -> validator.validate(b);
		assertThrows(MessageValidationException.class, e);

	}

}
