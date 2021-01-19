# A (Somewhat More) Practical Guide to Apache Spark

It goes without saying that data science, as a practice and an industry, has exploded over the last decade, with data science and data engineering become one of the most sought-after skills in the workplace today. As such, data science programs have also exploded in popularity, both in academia, and through various academy-like orgs like Metis, General Assembly, Datacamp, Udacity and the like.

If you gradudated from any of these programs, you've probably worked with most of common data science tools, like Jupyter notebooks, pandas, scikit, sci-py, on a localized or cloud-based environment. Though these tools are fantastic, as enterprises begin to collect, store, and analyze larger volumes of data, these classic tools won't suffice, you'll start running into the dreaded <code>pandas.io.common: out of memory</code> error. As such, many teams are making the jump to a distributed computing and analytics platform: Apache Spark.

In the following article, we'll cover key concepts and practical examples that will help any data scientist make the transition from the more traditional, localized data science tools (pandas, Jupyter notebooks, scikit-learn, etc.) to a distributed computing framework in Apache Spark.

Topics covered will include:

- Spark Internals and Core Spark Concepts (Master-Executor Framework, Partitions, Stages, and Jobs)
- Spark UI (SQL Plans, Executor/Job Metrics) 
- Spark Memory Management Basics 
- Various Data Serialization Formats

Please note, these articles are NOT meant to be a Spark programming tutorial. Though we will use and explain various code examples throughout, there are plenty of sources that cover the "how-tos" of Spark programming. These articles assume that you have a basic knowledge of Spark programming.

## Housekeeping Details
Throughout these articles, we will be using scala-spark for all of the code examples, following the following specs:

- Spark version 3.0.1
- Scala 2.12.11
- Java 11
- Apache Maven 3.6.3 for jar building

## From Localized to Distributed Computing

Conceptually, moving from localized to distributed computing is pretty simple: 

In order to handle very large datasets, or extremely memory-expensive tasks, we go from using a single machine (with dedicated memory, compute, and storage) we divide up the data into smaller chunks, and send those chunks to a cluster of machines, which execute tasks on that data in parallel.

This has several key advantages:

- 

## Spark and the Distributed Computing Framework
