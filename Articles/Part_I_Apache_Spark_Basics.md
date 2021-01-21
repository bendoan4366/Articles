## Apache Spark: Concepts and Basics

Apache Spark is widely regarded as the most popular open-source distributed computing tools, and provides powerful APIs that combine the distributed computing framework with familiar data structures, such as dataframes.

## Basic Terms

Before we get into the knitty-gritty details of how Spark works, we should first establish some basic terms:

### Distributed Computing Terms
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
