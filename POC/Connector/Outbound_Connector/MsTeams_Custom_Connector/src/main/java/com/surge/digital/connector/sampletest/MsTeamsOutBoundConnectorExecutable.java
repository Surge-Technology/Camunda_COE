package com.surge.digital.connector.sampletest;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@OutboundConnector(
    name = "Ms Teams Custom Out Bound Connector",
    inputVariables = {},
    type = "io.camunda:connector-custom:1")
public class MsTeamsOutBoundConnectorExecutable implements OutboundConnectorFunction {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MsTeamsOutBoundConnectorExecutable.class);

  RestTemplate restTemplate = new RestTemplate();

  @Override
  public Object execute(final OutboundConnectorContext context) throws Exception {
    String url = "http://localhost:8080/startFlow";

    String str = context.getVariables();

    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(str);

    String senderEmailId = (String) json.get("senderEmailId");

    String receieverEmailId = (String) json.get("receieverEmailId");

    String content = (String) json.get("body");

    String accessToken = (String) json.get("AccessToken");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + accessToken);
    headers.set("Content-Type", "application/json");

    String messageDto =
        "{\r\n"
            + "    \"senderEmailId\": \""
            + senderEmailId
            + "\",\r\n"
            + "    \"receieverEmailId\": \""
            + receieverEmailId
            + "\",\r\n"
            + "    \"accessToken\": \""
            + accessToken
            + "\",\r\n"
            + "    \"body\": {\r\n"
            + "        \"content\": \""
            + content
            + "\"\r\n"
            // " \"content\": \"Task Completed completed\" \r\n"
            + "    }\r\n"
            + "}";
    HttpEntity<String> requestEntitys = new HttpEntity<>(messageDto, headers);
    ResponseEntity<String> responses =
        restTemplate.exchange(url, HttpMethod.POST, requestEntitys, String.class);

    LOGGER.info("Executing ReportUrlApi connector with context: " + context);
    var connectorRequest = context.getVariables();
    context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);
    LOGGER.info("Hello there from " + context.getVariables());
    return context.getVariables();
  }
}
