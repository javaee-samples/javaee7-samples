package org.javaee7.jsf.flows.programmatic;

import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

/**
 * @author Arun Gupta
 */
public class Flow2 {

    @Produces @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        String flowId = "flow2";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").markAsStartNode();
        
        flowBuilder.returnNode("taskFlowReturn1").
                fromOutcome("#{flow2Bean.returnValue}");
        flowBuilder.returnNode("goHome").
                fromOutcome("#{flow2Bean.homeValue}");
        
        flowBuilder.inboundParameter("param1FromFlow1", "#{flowScope.param1Value}");
        flowBuilder.inboundParameter("param2FromFlow1", "#{flowScope.param2Value}");
        
        flowBuilder.flowCallNode("call1").flowReference("", "flow1").
                outboundParameter("param1FromFlow2", "param1 flow2 value").
                outboundParameter("param2FromFlow2", "param2 flow2 value");

        return flowBuilder.getFlow();
    }
}
