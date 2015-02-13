package org.javaee7.jsf.flows.programmatic;

import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

/**
 * @author Arun Gupta
 */
public class Flow1 {

    @Produces
    @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        String flowId = "flow1";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").markAsStartNode();

        flowBuilder.returnNode("taskFlowReturn1").
            fromOutcome("#{flow1Bean.returnValue}");
        flowBuilder.returnNode("goHome").
            fromOutcome("#{flow1Bean.homeValue}");

        flowBuilder.inboundParameter("param1FromFlow2", "#{flowScope.param1Value}");
        flowBuilder.inboundParameter("param2FromFlow2", "#{flowScope.param2Value}");

        flowBuilder.flowCallNode("call2").flowReference("", "flow2").
            outboundParameter("param1FromFlow1", "param1 flow1 value").
            outboundParameter("param2FromFlow1", "param2 flow1 value");

        return flowBuilder.getFlow();
    }
}
