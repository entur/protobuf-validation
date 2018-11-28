# Protobuf validation libary

This library allows you to add validation statements inside the proto files as options to fields.

It supports validation both standalone and as a gRPC client and server interceptor.

Based on initial work by SeriousMa: https://github.com/SeriousMa/grpc-protobuf-validation

# Usage

Add rules to your proto files:
```proto
import "validation.proto";
message ExampleMessage {
    string name = 1[(validation.regex) = "[A-Z]{2,}"];
}
```

## Standalone protobuf validation

```java
  ProtobufValidator validator = new ProtobufValidator();
  validator.validate(protobufJavaMessage);
```

An MessageValidationException is thrown in case of any validation errors.

## gRPC integration

Example shows gRPC server setup

### Create grpc server 
```java
 Server greetingServer = ServerBuilder.forPort(8080).addService(ServerInterceptors.intercept(new GreetingServiceImpl(), new ValidationInterceptor())).build();

```

### Create grpc channel

```java
  ManagedChannelBuilder builder = ManagedChannelBuilder.forAddress(url.getHost(), url.getPort()).usePlaintext(true);
  builder.intercept(new ValidationInterceptor());
```

# Supported rules

```proto
import "validation.proto";

message HelloRequest {
    string name = 1[(validation.regex) = ""];
    int32 age = 2 [(validation.max) = 100, (validation.min) = 18];
    repeated string hobbies = 3 [(validation.repeatMax) = 5, (validation.repeatMin) = 2];
    map<string, string> bagOfTricks = 4 [(validation.repeatMax) = 10, (validation.repeatMin) = 2];
    Sentiment sentiment = 5 [(validation.required) = true;
    int64 future_timemilles = 6 [(validation.future) = true];
    int64 past_timemilles = 7 [(validation.past) = true];
    string forbidden_field = 8 [(validation.forbidden) = true;
}
```
