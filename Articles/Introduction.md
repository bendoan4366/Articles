# A (Somewhat More) Practical Guide to Apache Spark - Part 1: Conceptual Introduction to Distributed Computing 

 In one of my first full-time roles as a data scientist, I was tasked with deploying and managing several Apache Spark pipelines that ingested, transformed, and cleaned terabytes of data for an enterprise analytics platform. 
 
 At the time, I had worked on a several academic projects using Spark, mainly through Databricks and AWS EMR. But going into this project, my primary experience was working with the usual data science python libraries (Jupyter notebook, pandas, scikit, numpy, sci-py, Keras, etc.) in both local and cloud-based environments (a combination of local Notebooks, Kaggle Notebooks, and JuptyerHub), so I was making the jump to distributed tools in a professional setting, for first time.
<br>
<br>
On my third day of the job, I reviewed the specifications for my new project, and I thought to myself: "....this should be an easy transition, right?"
<br>
<br>

 ![yesButNo](../graphics/Introduction/wellYesButNo.gif) 
<br>
<br>

At the surface level, there are a lot of similarites between Spark and the traditional DS tools that made the transition seem *somewhat* easy: 
- Pandas and Spark dataframes both provide intuitive, tabular representation for datasets (I happen to think that filtering and deduplications are actually *more* intuitive in Spark than Pandas, but more on that later)
- Both have one-line implementations for reading popular data formats (csv, parquet, json, txt)
- Both have similar UDF for implementations columnar transformations
- Scikit-learn and Spark MLlib are fairly similar in conceptual implementation (import-instantiate-fit-test-assess-repeat), though the code implementation differs slightly

However, a few more steps into the world of distributed computing revealed that there are <strong>many</strong> more considerations when implementing scalable, efficient jobs across a cluster. 

Many of these differences happen under the hood (we'll refer to this as *Spark Internals* going forward), and require knowledge of commodity machines, memory allocation, and distributed storage. As such, depending on where you learned DS initially (especially if you learned through a post-grad technical program, like me*), you probably didn't get full exposure to these topics.
<br>
<br>

 ![ohno](../graphics/Introduction/ohno.png)

 <br>

In the following articles, I'll be addressing many of these differences, covering key concepts and practical examples that will help any data scientist make the transition from the more traditional, localized data science tools to a distributed computing framework in Apache Spark.

Topics covered will include:

- A Conceptual Introduction to Distributed Computing (this article)
- Spark Internals and Core Spark Concepts (Master-Executor Framework, Partitions, Stages, and Jobs)
- Spark UI (SQL Plans, Executor/Job Metrics) 
- Spark Memory Management Basics 
- Various Data Serialization Formats
- MLlib Optimizations

<br>

Now let's get started.
<br>
<br>

## A Conceptual Introduction to Distributed Computing

This next section goes over a <strong>conceptual</strong> explanation of distributed computing, as opposed to local/single threaded computing. This serves to establish a mental framework to carry forward, into the next few Spark-specific articles. As such, this will be an *abstract* description of distributed processes and not specific to Apache Spark.

If you want to jump right into Spark, please continue to the [next article](link_to_Part_I_article)
<br>
<br>

### From Localized to Distributed Computing

Conceptually, the rationale for moving from localized to distributed computing is pretty simple: 

- Single computers (or *machines* going forward) have limited amounts of memory, computing power, and storage. So if you want to process an amount of data that is greater than your memory and storage capabilties, your machine will struggle (and likely fail)
- So, instead of using a single computer, we can break the data into chunks and use multiple machines (or a *cluster* of machines) to divide the up work on those data chunks. We call the individual computers/machines in a cluster *nodes* 
- Using the method above, we can harness the combined memory, computing, and storage of all the machines in the cluster, and we *distribute* the work across these machines. This allows us to handle significantly larger data sizes and perform very complex tasks on this data.
<br>

### Key Terms
- <Strong>Machine</Strong>: Another term for a computer or computing hardware (I will use them synonomously throughout)
- <Strong>Cluster</Strong>: Group of machines
- <Strong>Node</Strong>: An individual machine in a cluster
- <Strong>Master Node</Strong>: An node (or application deployed on a node) that's responsible for managing the distributed process
- <Strong>Worker Node</Strong>: Receives process instructions from the master and carries out those instructions by performing tasks and on the dataset
- <Strong>Resource Manager</Strong>: Works with the Master Node ensure that there are enough available worker nodes to execute the distributed process

### Conceptual Steps
For any distributed computing process, the steps to execute that process across are generally as follows:

1. We start with a set of data and some tasks we want completed, using that data. We compile those tasks into instructions (an *application*) and send the application to the Master node.
2. The Master works with a Resource Manager to secure the appropriate number of Worker nodes, to ensure the application can be run
3. The Master application reads and optimizes the instructions within the application, and sends these instructions to each Worker
4. Each Worker then reads a chunk of the data, (per instructions from the Master) and executes the tasks according to the instructions sent to it
5. Workers then report progress back to the Master, and the final result is presented to us through the Master node


This has several key advantages:

- The amount of total available memory, storage, and compute across a cluster is significantly higher than a single machine
- Distributed computing processes <strong>scale</strong> much more easily. That is, if you need more memory or computing power, you can  add more machines to your cluster. This is opposed to a single machine (which is what would be used for traditional DS tools), where you would need to add RAM to the device or upgrade the processor
- Many distributed processes implement parallelism, in which certain tasks are completed concurrently on different chunks of data. This speeds up data processing significantly, especially for memory-intensive tasks

However, with more machines also comes more processes to manage them, and thus more places for errors. Below, I've categorized the errors into "buckets"(but it's worth noting that there WAY more errors, than I have time or space to account for):

- Network/Communication Errors
- Executor Node Errors
- Master Node Errors

### Additional Notes
*Earlier I mentioned that a lot of DS post-grad programs (General Assembly, Metis, Udacity, etc.) don't cover these topics extensively. That's not to say that these aren't fantastic programs. I personally graduated from the General Assembly Immersive Data Science program in 2017 and Udacity Data Engineering Nanodegree Program in 2020, and can vouch for the quality of their curriculums and material. There just simply aren't enough hours to cover everything 