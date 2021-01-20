# A (Somewhat More) Practical Guide to Apache Spark

 In one of my first full-time roles as a data scientist, I was tasked with deploying and managing several Apache Spark pipelines that ingested, transformed, and cleaned terabytes of data for an enterprise analytics platform. 
 
 At the time, I had worked on a several academic projects using Spark, mainly through Databricks and AWS EMR. But going into this project, my primary experience was working with the usual data science python libraries (Jupyter notebook, pandas, scikit, numpy, sci-py, Keras, etc.) in both local and cloud-based environments (a combination of local Notebooks, Kaggle Notebooks, and JuptyerHub). 
 
 In summary, I had primarily worked with single-threaded tools, in local and cloud environments, making the jump to distributed tools in a professional setting, for first time.
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

Many of these differences happen under the hood (dubbed *Spark Internals* going forward), and require knowledge of commodity machines, memory allocation, and distributed storage. As such, depending on where you learned DS initially (especially if you learned through a post-grad technical program, like me*), you probably didn't get full exposure to these topics.
<br>
<br>

 ![ohno](../graphics/Introduction/ohno.png)

 <br>

In the following articles, I'll be addressing many of these differences, covering key concepts and practical examples that will help any data scientist make the transition from the more traditional, localized data science tools to a distributed computing framework in Apache Spark.

Topics covered will include:

- Spark Internals and Core Spark Concepts (Master-Executor Framework, Partitions, Stages, and Jobs)
- Spark UI (SQL Plans, Executor/Job Metrics) 
- Spark Memory Management Basics 
- Various Data Serialization Formats
- MLlib Optimizations

## Housekeeping Details

Throughout these articles, we will be using scala-spark for all of the code examples, using the following specs:

- Spark version 3.0.1
- Scala 2.12.11
- Java 11
- Apache Maven 3.6.3 for jar building

### Additional Notes
- These articles are NOT meant to be a Spark programming tutorial. Though we will use/explain various code examples throughout, there are plenty of sources that cover the "how-tos" of Spark programming. These articles assume that you have a basic knowledge of Spark programming, or at least know how to look up basic code documentation
- I am NOT claiming that Apache Spark is better than Pandas or any of the common DS tools. I firmly believe that each toolkit is optimal for different use cases, data sizes, and team sizes. No one tool is comprehensively better than the others.
- *Earlier I mentioned that a lot of DS post-grad programs (General Assembly, Metis, Udacity, etc.) don't cover these topics extensively. That's not to say that these aren't fantastic programs. I personally graduated from the General Assembly Immersive Data Science program in 2017 and Udacity Data Engineering Nanodegree Program in 2020, and can vouch for the quality of their curriculums and material. There just simply aren't enough hours to cover everything


<br>
Now let's get started.
<br>
<br>

## From Localized to Distributed Computing

Conceptually, moving from localized to distributed computing is pretty simple: 

In order to handle very large sets of data, or extremely memory-intensive tasks, instead of using a single computer (with limited memory, computing power, and storage), we use a cluster of multiple computers to break up the work and reduce the load on any single machine.

The high-level steps are as follows:

1. Once we decide what we want to do with the data, we send our instructions to a *Driver* machine
2. The Driver optimizes those instructions to be as efficient as possible
2. The Driver divides the data into chunks sends those chunks to the other *executor* machines. 
3. The executors complete the tasks on the chunks of data assigned to them (per instructions from the driver)
4. Executors report progress back to the driver, which is then presented to us.

This has several key advantages:

- The amount of total available cluster memory, storage, and compute is significantly higher than a single machine
- Memory and compute power for Spark processes <strong>scale</strong> much more easily. If you need more memory or compute, you can  add more machines to your cluster. This is opposed to a single machine (which is what would be used for traditional DS tools), where you would need to add RAM to the device or upgrade the processor
- Parallelism, which is the process of completing tasks concurrently, speeds up data processing significantly, especially for memory-intesive tasks

## Spark and the Distributed Computing Framework
