package com.example.demo.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class EndpointFunctionClass {

    /*
        Install Azure CLI on Windows
        Install Core Tools
        Install dotnet

        mvn clean package
        mvn azure-functions:package
        mvn azure-functions:run
    *
    * */

    @FunctionName("EndpointFunction")
    public HttpResponseMessage EndpointFunction(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        String name = request.getBody().orElse(request.getQueryParameters().get("name"));

        String responseMessage = name != null ? "Hello, " + name + "!" : "Hello, World!";

        return request.createResponseBuilder(HttpStatus.OK).body(responseMessage).build();
    }

    @FunctionName("TimerFunction")
    public void TimerFunction(
            @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo, ExecutionContext context) {
        context.getLogger().info("Timer trigger function executed at: " + java.time.LocalDateTime.now());
    }


    @FunctionName("ServiceBusQueueTriggerFunction")
    public void serviceBusQueueHandler(
            @ServiceBusQueueTrigger(name = "message", queueName = "myqueue", connection = "ServiceBusConnection") String message,
            ExecutionContext context) {
        context.getLogger().info("Service Bus Queue trigger function processed a message: " + message);
    }


    // Trabaja con Azure Storage Queues
    @FunctionName("QueueFunction")
    public void QueueFunction(
            @QueueTrigger(name = "message", queueName = "myqueue-items", connection = "AzureWebJobsStorage") String message,
            ExecutionContext context) {
        context.getLogger().info("Queue trigger function processed a message: " + message);
    }




}
