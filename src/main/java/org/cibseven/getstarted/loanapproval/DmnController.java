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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vavr.Tuple2;
import io.vavr.jackson.datatype.VavrModule;

@RestController
public class DmnController {
	
    @GetMapping("/VavrMapTo")
    public String mapTo()
    {
    	SpinJsonNode json = Spin.JSON("{\"customer\": {\"name\": \"Kermit\", \"age\": 35}, \"preferences\": {\"language\": \"en\", \"timezone\": \"UTC\"}}");

    	CustomerContext ctx = json.mapTo(CustomerContext.class);

    	System.out.println("Customer name: " + ctx.customer.name);
    	System.out.println("Age: " + ctx.customer.age.getOrElse(-1));
    	System.out.println("Language: " + ctx.preferences.language);
    	
    	return "Customer name: " + ctx.customer.name;
    }
}

