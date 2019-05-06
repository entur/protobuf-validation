package no.entur.grpc.interceptor.client;

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

import com.google.protobuf.GeneratedMessageV3;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import no.entur.protobuf.validation.MessageValidationException;
import no.entur.protobuf.validation.ProtobufValidator;

/**
 * @author Serious
 * @author seime
 * @date 2017/6/26
 */
public class ValidationInterceptor implements ClientInterceptor {

	private ProtobufValidator validator;


	public ValidationInterceptor(ProtobufValidator validator) {
		this.validator = validator;
	}

	public ValidationInterceptor() {
		this(ProtobufValidator.globalValidator());
	}


	@Override
	public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
		return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
			@Override
			public void sendMessage(ReqT message) {
				GeneratedMessageV3 protoMessage = (GeneratedMessageV3) message;
				try {
					validator.validate(protoMessage);
					super.sendMessage(message);
				} catch (MessageValidationException e) {
					Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
					throw new StatusRuntimeException(status);
				}
			}
		};
	}

}
