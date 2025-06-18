package org.cibseven.getstarted.loanapproval;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.cibseven.bpm.dmn.engine.*;
import org.cibseven.bpm.engine.variable.*;
import org.cibseven.bpm.engine.variable.Variables;
import org.cibseven.spin.Spin;
import org.cibseven.spin.json.SpinJsonNode;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;


@RestController
public class DmnController {

    @GetMapping("/evaluate")
    public String evaluate() {
    	String dmnXml ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    		    "<definitions id=\"definitions\"" +
    		    "             name=\"Definition\"" +
    		    "             namespace=\"http://camunda.org/schema/1.0/dmn\"" +
    		    "             xmlns=\"http://www.omg.org/spec/DMN/20151101/dmn.xsd\"" +
    		    "             xmlns:camunda=\"http://camunda.org/schema/1.0/dmn\">" +
    		    "  <decision id=\"decisionId\" name=\"Simple FEEL\">" +
    		    "    <decisionTable id=\"decisionTableId\" hitPolicy=\"FIRST\">" +
    		    "      <input id=\"input1\" label=\"Customer\">" +
    		    "        <inputExpression id=\"inputExpression1\" typeRef=\"string\">" +
    		    "          <text>jsonVariable.customer.name</text>" +
    		    "        </inputExpression>" +
    		    "      </input>" +
    		    "      <input id=\"input2\" label=\"Language\">" +
    		    "        <inputExpression id=\"inputExpression2\" typeRef=\"string\">" +
    		    "          <text>jsonVariable.preferences.language</text>" +
    		    "        </inputExpression>" +
    		    "      </input>" +
    		    "      <output id=\"output1\" label=\"Greeting\" name=\"greeting\" typeRef=\"string\"/>" +
    		    "      <rule id=\"ruleId\">" +
    		    "        <inputEntry id=\"inputEntry1\">" +
    		    "          <text>\"Kermit\"</text>" +
    		    "        </inputEntry>" +
    		    "        <inputEntry id=\"inputEntry2\">" +
    		    "          <text>\"en\"</text>" +
    		    "        </inputEntry>" +
    		    "        <outputEntry id=\"outputEntry1\">" +
    		    "          <text>\"Hello Kermit!\"</text>" +
    		    "        </outputEntry>" +
    		    "      </rule>" +
    		    "    </decisionTable>" +
    		    "  </decision>" +
    		    "</definitions>";
    	
        // 1. Create DMN Engine
        DmnEngine dmnEngine = DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();
        
        // 2. Load DMN from the string
        ByteArrayInputStream dmnStream = new ByteArrayInputStream(dmnXml.getBytes(StandardCharsets.UTF_8));
        DmnDecision decision = dmnEngine.parseDecision("decisionId", dmnStream);

        // 3. Create JSON variable
        SpinJsonNode json = Spin.JSON("{\"customer\": {\"name\": \"Kermit\", \"age\": 35}, \"preferences\": {\"language\": \"en\", \"timezone\": \"UTC\"}}");

        // 4. Add to variable map	
        VariableMap variables = Variables.createVariables().putValue("jsonVariable", json);

        // 5. Evaluate decision
        DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, variables);

        return result == null ? "No result" : result.getSingleResult().getEntryMap().toString();
    }
}

