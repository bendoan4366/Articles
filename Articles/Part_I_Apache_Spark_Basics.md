# Apache Spark and YARN: Concepts and Basics

In the [previous article](link_to_Part_I_article), we briefly discussed the differences between distributed data science and localized data science, to establish a framework to help understand Apache Spark.

Apache Spark is widely regarded as the most popular open-source distributed data science tool, and provides powerful APIs that combine the distributed computing framework with familiar data structures, such as dataframes. In this article, we will go over the basics of Spark internals, and how it works with a resource negotiator (YARN) to execute a basic Spark job.

## Spark vs. YARN: Software vs. Hardware Management
As mentioned in the [previously](link_to_Part_I_article), Spark is often run on a cluster of computers. This means that with each Spark job, a cluster must be secured and to nodes have to be organized to distribute execute the data-processing instructions (or Spark program).

<br>

This is where YARN comes in.

<br>

## Spark and YARN (Yet Another Resource Negotiator)

YARN (Yet Another Resource Negotiator) is a distributed  computing tool, developed by Apache, that is designed for cluster management, resource scheduling, and distributed job tracking. In other words, its main use is organize and arbitrate node resources in a cluster whenever a Spark job is started. 

<br>

### YARN Key Terms

Before we get into the knitty-gritty details of how Spark works, we should first establish some basic terms:

- <strong>Resource Manager</strong>: A node with the authority to allocate and coordinate other nodes and applications to execute an Spark job
- <strong>Container</strong>: An application "wrapper" that presides on a node. Containers house different types of applications and have computing units to perform specific tasks
- <strong> Application Master</strong>: Spark job-specific entity (that's housed in a container) that negotiates resources with the Resource Manager, and sends Spark job instructions to node managers for execution. Application Masters are also housed in containers
- <strong>Node Manager</strong>: An application on a node (also housed in a container) that communicates with the Application master and manages other containers on the node to complete instructions sent to it from the Application Master
- <strong>Executor</strong>: Compute units (wrapped in containers) that execute the steps in a Spark Job. When you write a Spark program, executors are what actually carry out the task. Everything above just sets up the environment for that allows executors to complete their work. You can manually allocate cores and memory to each executor in your Spark job parameters (more on this below)

Below, you can find a graphic that details the YARN architecture using the terms above:

![ohno](../graphics/Pt_I_YARN_Spark/Yarn-Arch.png)


<br>

## Basic Terms

Before we get into the knitty-gritty details of how Spark works, we should first establish some basic terms:

### Distributed Computing Terms
### Key Terms
- <Strong>Machine</Strong>: Another term for a computer or computing hardware (I will use them synonomously throughout)
- <Strong>Cluster</Strong>: Group of machines
- <Strong>Node</Strong>: An individual machine in a cluster
- <Strong>Master Node</Strong>: An node (or application deployed on a node) that's responsible for managing the distributed process
- <Strong>Worker Node</Strong>: Receives process instructions from the master and carries out those instructions by performing tasks and on the dataset
- <strong>Resource Manager</strong>: An application that resides on a node with the authority to allocate 
- <strong>Node Manager</strong>: An application on a node that communicates with the resource manager
- <strong>Application Master</strong>: A master
- <strong>Container</strong>: A wrapper placed around executors, application masters, drivers, and 

### Spark-Specific Terms
- <strong>Partition</strong>: Refers to a chunk or block of data, as described above
- <strong>Task</strong>: A command or step to be completed on a partition
- <strong>Stage</strong>: Collection of tasks logically divided based on function and processing priority (more on this later)
- <strong>Job</strong>: An end-to-end computation process, comprised of stages and tasks. Jobs are triggered by Spark actions 
- <strong>Spark Driver</strong>
    - <strong>Driver Cores</strong>
    - <strong>Driver Memory</strong>
- <strong>Executor</strong>: A single computing unit used to execute a task
    - <strong>Executor Cores</strong>: Number of cores allocated to an executor
    - <strong>Executor Memory</strong>: Amount of memory allocated to an executor


## Spark Execution

The high-level steps are as follows:

1. Once we decide what we want to do with the data, we send our instructions to a *Driver* machine
2. The Driver optimizes those instructions to be as efficient as possible
2. The Driver divides the data into chunks sends those chunks to the other *executor* machines. 
3. The executors complete the tasks on the chunks of data assigned to them (per instructions from the driver)
4. Executors report progress back to the driver, which is then presented to us.
