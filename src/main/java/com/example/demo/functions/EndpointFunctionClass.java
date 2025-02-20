package com.example.demo.functions;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EndpointFunctionClass {

    /*
        Install Azure CLI on Windows
        Install Core Tools
        Install dotnet

        Instala azurite:
        npm install -g azurite
        azurite --silent --location c:\azurite --debug c:\azurite\debug.log

        Programa para desarrollo de colas en local:
        https://azure.microsoft.com/es-es/products/storage/storage-explorer

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

        context.getLogger().info("*** INICIANDO HTTP-TRIGGER ***");

        String name = request.getBody()
                .orElse(request.getQueryParameters().get("name"));

        String responseMessage = name != null ? "Hello, " + name + "!" : "Hello, Default!";

        return request.createResponseBuilder(HttpStatus.OK).body(responseMessage).build();
    }

    @FunctionName("TimerFunction")
    public void TimerFunction(
            @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo, ExecutionContext context) {
        context.getLogger().info("*** INICIANDO TIME-TRIGGER A LAS " + java.time.LocalDateTime.now() + " ***");
    }


    @FunctionName("QueueFunction")
    public void QueueFunction(
            // AZURE STORAGE QUEUE
            @QueueTrigger(name = "message", queueName = "cola-function-local-test", connection = "AzureWebJobsStorage") String message,
            ExecutionContext context) {
        context.getLogger().info("*** INICIANDO QUEUE-TRIGGER CON EL SIGUIENTE MENSAJE " + message + " ***");
    }

    @FunctionName("BlobTriggerFunction")
    public void BlobTriggerFunction(
            @BlobTrigger(
                    name = "content",
                    path = "contenedor-test-function/{name}",
                    dataType = "binary",
                    connection = "AzureWebJobsStorage"
            )
            byte[] content,
            @BindingName("name") String filename,
            final ExecutionContext context
    ) {
        context.getLogger().info("*** INICIANDO BLOB-TRIGGER CON EL SIGUIENTE ARCHIVO: " + filename + " ***");

        try {
            // Muestra el tamaño del archivo en bytes
            context.getLogger().info("Tamaño del archivo: " + content.length + " bytes");

            // Aquí puedes agregar tu lógica de procesamiento, por ejemplo:
            // - Guardarlo en otra ubicación
            // - Enviarlo a otro servicio
            // - Analizar su contenido, etc.

        } catch (Exception e) {
            context.getLogger().severe("Error procesando el blob: " + e.getMessage());
        }
    }



//    @FunctionName("ServiceBusQueueTriggerFunction")
//    public void serviceBusQueueHandler(
//            @ServiceBusQueueTrigger(name = "message", queueName = "myqueue", connection = "ServiceBusConnection") String message,
//            ExecutionContext context) {
//        context.getLogger().info("Service Bus Queue trigger function processed a message: " + message);
//    }




}
