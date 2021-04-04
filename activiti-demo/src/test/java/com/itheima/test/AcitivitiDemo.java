package com.itheima.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.varia.LevelRangeFilter;
import org.junit.Test;

import java.io.*;
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

    @Test
    public void queryProcessDefinition() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery definitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> definitionList = definitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义ID：" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义Key：" + processDefinition.getKey());
            System.out.println("流程定义版本：" + processDefinition.getVersion());
            System.out.println("流程部署ID：" + processDefinition.getDeploymentId());
        }
    }

    @Test
    public void deleteDeployMent() {
//      获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        通过引擎来获取 RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        通过部署id来删除流程部署信息
        String deploymentId = "1";
//        repositoryService.deleteDeployment(deploymentId);
        repositoryService.deleteDeployment(deploymentId);
    }

    @Test
    public void getDeployment() throws IOException {
//         1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、获取api，RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、获取查询对象 ProcessDefinitionQuery查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
//        4、通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
//        5、通过RepositoryService ，传递部署id参数，读取资源信息（png 和 bpmn）
//          5.1、获取png图片的流
//        从流程定义表中，获取png图片的目录和名字
        String pngName = processDefinition.getDiagramResourceName();
//        通过 部署id和 文件名字来获取图片的资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
//          5.2、获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
//        6、构造OutputStream流
        File pngFile = new File("/Users/yujinhai/evectionflow01.png");

        File bpmnFile = new File("/Users/yujinhai/evectionflow01.bpmn");
        FileOutputStream pngOutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutStream = new FileOutputStream(bpmnFile);
//        7、输入流，输出流的转换
        IOUtils.copy(pngInput,pngOutStream);
        IOUtils.copy(bpmnInput,bpmnOutStream);
//        8、关闭流
        pngOutStream.close();
        bpmnOutStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
//      获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
//        获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
//        查询 actinst表，条件：根据 InstanceId 查询
//        instanceQuery.processInstanceId("2501");
//        查询 actinst表，条件：根据 DefinitionId 查询
        instanceQuery.processDefinitionId("myEvection:1:4");
//        增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
//        查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
//        输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<==========================>");
        }
    }
}