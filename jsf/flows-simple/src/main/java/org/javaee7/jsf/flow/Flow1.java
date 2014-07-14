package org.javaee7.jsf.flow;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

/**
 * @author Arun Gupta
 */
public class Flow1 implements Serializable {

    private static final long serialVersionUID = -7623501087369765218L;

//    @Produces @FlowDefinition
//    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
//        String flowId = "flow1";
//        flowBuilder.id("", flowId);
//        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").markAsStartNode();
//
//        return flowBuilder.getFlow();
//    }
}
