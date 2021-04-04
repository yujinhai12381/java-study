package com.itheima.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class AcitivitiDemo {
    @Test
    public void testDepolyment() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().name("出差流程申请").
                addClasspathResource("bpmn/evection.bpmn").
                addClasspathResource("bpmn/evection.png").
                deploy();

        System.out.println("流程部署ID"+deploy.getId());
        System.out.println("流程部署Name"+deploy.getName());
    }

    @Test
    public void testStartProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");

        System.out.println("流程定义ID："+instance.getProcessDefinitionId());
        System.out.println("流程实例ID："+instance.getId());
        System.out.println("当前活动的ID："+instance.getActivityId());

    }

    @Test
    public void testFindPersonalTaskList() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("myEvection").
                taskAssignee("zhangsan").
                list();
        for (Task task : taskList) {
            System.out.println("流程实例id="+task.getProcessInstanceId());
            System.out.println("任务Id="+task.getId());
            System.out.println("任务负责人="+task.getAssignee());
            System.out.println("任务名称="+task.getName());
        }

    }

    @Test
    public void completTest() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
//        taskService.complete("2505");
        //完成Jerry任务
//        Task task = taskService.createTaskQuery().
//                processDefinitionKey("myEvection").
//                taskAssignee("jerry").singleResult();
        //完成Jack任务
//        Task task = taskService.createTaskQuery().
//                processDefinitionKey("myEvection").
//                taskAssignee("jack").singleResult();
        //完成rose任务
        Task task = taskService.createTaskQuery().
                processDefinitionKey("myEvection").
                taskAssignee("rose").singleResult();
        System.out.println("流程实例id="+task.getProcessInstanceId());
        System.out.println("任务Id="+task.getId());
        System.out.println("任务负责人="+task.getAssignee());
        System.out.println("任务名称="+task.getName());
        taskService.complete(task.getId());


    }

}