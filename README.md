# Fastx-AI LLM Rag System

In the rapidly evolving landscape of artificial intelligence, knowledge bases have emerged as a critical component for driving innovation and enabling intelligent decision-making. Our open-source project, AI Knowledge Base, aims to build a flexible, scalable, and easily accessible platform that provides robust support for developers and researchers.

The goal of the AI Knowledge Base project is to create an infrastructure that supports efficient knowledge retrieval and management by integrating multiple data sources, semantic analysis, and natural language processing technologies. This platform will empower developers to build context-aware intelligent applications more effectively, whether it be automated customer service systems, personalized recommendation engines, or complex decision support tools.

The open-source nature of the project not only fosters rapid iteration and innovation but also encourages collaboration and sharing within the global developer community. We welcome contributions from developers of all backgrounds to enhance this essential component of future AI applications by sharing code and expertise.

Join us and let the AI Knowledge Base become the cornerstone of your next intelligent application, helping to elevate AI technology to new heights.

## Architecture

## Self Hosting

We are very excited for you to deploy our application!

### Jar Deploy

### Docker Deploy

noticed that `seata-server` folder contains seata server config file which you need mound to your seata docker container use `-v` and here for more [seata docker deploy](https://seata.apache.org/docs/ops/deploy-by-docker).
noticed that `mysql` need use latest version for this project because we use `JSON`, `ENUM` ... futures in this project.
noticed that `it's a dev branck now`, do not use in production env.

- mysql
- zookeeper
- seata server
- redis

## Other

## Thanks For OpenSources

- Dubbo/Sentinel
- Mybatis/Plus/P6spy
- Spring/Boot
- Zookeeper
- Redis/Redisson
- Seata
- AutoService/Annotation