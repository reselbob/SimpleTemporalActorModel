# Understanding a Temporal workflow as an implementation of the Actor Model

The project demonstrates how key features of the Actor Model map onto Temporal Workflows.

| NOTE                                                                                                                                                                                                                                                   |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| In order to get full benefit from using this project, it's useful to have a basic understanding of what Temporal is and how it works. [This overview](https://docs.temporal.io/temporal) provides a good basic introduction to the Temporal framework. |


In the context of the [Actor Model](https://temporal.io/blog/workflows-as-actors-is-it-really-possible), the sample 
project treats a [Temporal Workflow](https://docs.temporalio/workflows)
as an actor. Temporal [Signals](https://docs.temporalio/encyclopedia/application-message-passing#signals) are considered
custom messages sent to the actor, (the Temporal Workflow) to trigger specific behavior.

The sample project contains a Temporal Workflow and constituent Child Workflow. Interaction with the Workflow is 
conducted using Temporal's Signal feature.

The figure below illustrates the basic structure of the demonstration application.

![workflow-actor-model-02](https://github.com/reselbob/SimpleTemporalActorModel/assets/1110569/561bca59-2c79-4481-a28d-d83717e26477)


---

# Installing the code

To execute the code, take the following steps:

The [Java Virtual Machine](https://openjdk.org/) and [Maven](https://maven.apache.org/install.html) need to be installed
on the host computer.

## (1) Confirm that Java and Maven are installed on the host machine

Confirm that Java is installed:

```bash
java --version
```

You'll get output similar to the following:

```bash
openjdk 18.0.2-ea 2022-07-19
OpenJDK Runtime Environment (build 18.0.2-ea+9-Ubuntu-222.04)
OpenJDK 64-Bit Server VM (build 18.0.2-ea+9-Ubuntu-222.04, mixed mode, sharing)
```

Confirm that Maven is installed:

```bash
mvn --version
```

```bash
Maven home: /usr/share/maven
Java version: 18.0.2, vendor: Oracle Corporation, runtime: /usr/lib/jvm/jdk-18.0.2
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.19.0-46-generic", arch: "amd64", family: "unix"
```

---

## (2) Download and install the Temporal CLI (which includes the Service)

If you do not have the Temporal Service installed, click the link below to go to the Temporal documentation that has the
instructions for installing the Temporal CLI.

[https://docs.temporal.io/cli/#installation](https://docs.temporal.io/cli/#installation)

The Temporal development server ships with the CLI.

---

## (3) Start the Temporal Service

Here is the command for starting the Temporal Service on a local Ubuntu machine. Execute the command in a separate
terminal
window.

```bash
temporal server start-dev
```

---

## (4) Do some maven housecleaning

Run the following command in a new terminal window to create a fresh Maven environment:

```bash
mvn clean package install
```

## (5) Start the application

In that same terminal window run:

```bash
mvn exec:java -Dexec.mainClass="temporal.App"
```

You'll see output, similar to the following:

```
15:04:49.813 [temporal.App.main()] INFO  i.t.s.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
15:04:50.043 [temporal.App.main()] INFO  io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="SimpleWorkflow", namespace="default", identity=61479@bobs-mac-mini.lan}
15:04:50.046 [temporal.App.main()] INFO  io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="SimpleWorkflow", namespace="default", identity=61479@bobs-mac-mini.lan}
15:04:50.048 [temporal.App.main()] INFO  temporal.App - Worker listening on task queue: SimpleWorkflow.
15:04:50.150 [workflow-method-SimpleWorkflow-01-fe3d1040-cff4-4b58-b17b-cc3e3e383d76] INFO  temporal.SimpleWorkflowImpl - Starting Workflow for SimpleWorkflow
15:04:50.179 [signal add] INFO  temporal.SimpleWorkflowImpl - Order added in Workflow:{"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"PURCHASE", "message":"MV3H2ANVAO"}
15:04:50.193 [signal update] INFO  temporal.SimpleWorkflowImpl - Order updated in Workflow: {"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"MODIFY", "message":"MV3H2ANVAO"}
15:04:50.199 [signal notifyCustomer] INFO  temporal.SimpleWorkflowImpl - Notifying customer for order from parent workflow: {"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"MODIFY", "message":"MV3H2ANVAO"}
15:04:50.221 [workflow-method-ChildWorkflow_01-98928d6e-8f08-465a-90e7-4fdbd366a4d4] INFO  temporal.ChildWorkflowImpl - Starting Workflow for ChildWorkflow
15:04:50.224 [Activity Executor taskQueue="SimpleWorkflow", namespace="default": 1] INFO  t.OrderProcessingActivityImpl - Order updated in Activity: {"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"MODIFY", "message":"MV3H2ANVAO"}
15:04:50.224 [Activity Executor taskQueue="SimpleWorkflow", namespace="default": 2] INFO  t.OrderProcessingActivityImpl - Order added in Activity: {"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"PURCHASE", "message":"MV3H2ANVAO"}
15:04:50.241 [signal sendNotification] INFO  temporal.ChildWorkflowImpl - Sending notification for customer from child workflow: {"orderId":"c2ea65e6-2d06-46da-bd52-8816b99355ff", "customerId":"7f5922c1-9793-4c6c-8761-26fb9bfc589a", "updateType":"MODIFY", "message":"MV3H2ANVAO"}
15:04:55.129 [signal exit] INFO  temporal.SimpleWorkflowImpl - Exiting Workflow for SimpleWorkflow
```

## (6) View the Temporal Web Console

After you get the application running, open a browser and go to the following URL:

```bash
http://localhost:8233/
```

You'll see the Temporal Web Console. Click on the `Workflow-01` link to view the timeline and event history of the 
controller workflow. Click on the `ChildWorkflow_01` link to view the timeline and event history of the child workflow.

![Web-UI-01](https://github.com/reselbob/SimpleTemporalActorModel/assets/1110569/41af1395-1ebd-4a1a-9f75-f99250709fde)

**Temporal Web UI**


