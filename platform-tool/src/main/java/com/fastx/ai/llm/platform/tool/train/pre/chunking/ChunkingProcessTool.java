package com.fastx.ai.llm.platform.tool.train.pre.chunking;

import com.alibaba.fastjson2.JSON;
import com.fastx.ai.llm.platform.tool.entity.Fields;
import com.fastx.ai.llm.platform.tool.entity.Prototype;
import com.fastx.ai.llm.platform.tool.spi.IPlatformTool;
import com.fastx.ai.llm.platform.tool.train.TrainInput;
import com.fastx.ai.llm.platform.tool.train.TrainOutput;
import com.fastx.ai.llm.platform.tool.train.pre.BasePreTrainTool;
import com.fastx.ai.llm.platform.tool.train.pre.chunking.input.ChunkingInput;
import com.google.auto.service.AutoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author stark
 */
@AutoService(IPlatformTool.class)
public class ChunkingProcessTool extends BasePreTrainTool {

    static Prototype _prototype = new Prototype();

    static {
        List<Fields> config = new ArrayList<>();
        // for inputs.
        List<Fields> inputs = new ArrayList<>();
        inputs.add(Fields.of("input", String.class, "hello world"));
        inputs.add(Fields.of("chunkSize", Integer.class));

        // for outputs.
        List<Fields> outputs = new ArrayList<>();
        outputs.add(Fields.of("chunks", List.class));

        // add to
        _prototype.setConfig(config);
        _prototype.setInputs(inputs);
        _prototype.setOutputs(outputs);
    }

    @Override
    public TrainOutput exec(TrainInput input) {
        try {
            ChunkingInput pyInput = JSON.parseObject(input.getInputs(), ChunkingInput.class);
            Assert.isTrue(!StringUtils.isEmpty(pyInput.getInput()), "input can not be empty");
            List<String> chunks = new ArrayList<>();
            // process chunks!
            Integer chunkSize = pyInput.getChunkSize();
            String markdown = pyInput.getInput();

            List<String> sepChunks = new ArrayList<>();
            for (String s : Arrays.asList(markdown.split(""))) {
                if (sepChunks.size() <= chunkSize) {
                    // add to chunk
                    sepChunks.add(s);
                } else {
                    // clean chunk, save current chunk to
                    String joined = String.join("", sepChunks);
                    chunks.add(joined);
                    sepChunks.clear();
                }
            }

            return TrainOutput.of(JSON.toJSONString(Map.of("chunks", chunks)), null);
        } catch (Exception e) {
            return TrainOutput.of(null, e.getMessage());
        }
    }

    private List<String> readOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines()
                    .collect(Collectors.toList());
        }
    }

    @Override
    public String getName() {
        return "Text Chunk";
    }

    @Override
    public String getDescription() {
        return "Chunk TextInput like 'Hello world' to [A, B, C].";
    }

    @Override
    public String getCode() {
        return "train.pre.file.text.chunk";
    }

    @Override
    public String getPrototype() {
        return _prototype.toJSONString();
    }

    public static void main(String[] args) {
        TrainInput input = new TrainInput();
        input.setInputs(JSON.toJSONString(Map.of("input", "# Fastx-AI LLM RAG System\n" +
                "\n" +
                "![alt gif from: dailydoseofds](./resources/agents.gif)\n" +
                "<p align=\"center\" style=\"color: #eeeeee\">RAG build on agents; gif: dailydoseofds</p>\n" +
                "\n" +
                "## Introduction\n" +
                "\n" +
                "In the rapidly evolving landscape of artificial intelligence, knowledge bases have emerged as a critical component for driving innovation and enabling intelligent decision-making. The **Fastx-AI LLM RAG System** is an open-source project designed to build a flexible, scalable, and easily accessible platform that provides robust support for developers and researchers.\n" +
                "\n" +
                "The primary goal of the Fastx-AI LLM RAG System is to create an infrastructure that supports efficient knowledge retrieval and management by integrating multiple data sources, semantic analysis, and natural language processing technologies. This platform empowers developers to build context-aware intelligent applications more effectively, whether it be automated customer service systems, personalized recommendation engines, or complex decision support tools.\n" +
                "\n" +
                "## Key Features\n" +
                "\n" +
                "- **Multi-Source Integration**: Seamlessly integrate various data sources, including databases, APIs, and unstructured text.\n" +
                "- **Semantic Analysis**: Utilize advanced NLP techniques to understand and process natural language queries.\n" +
                "- **Scalability**: Designed to handle large volumes of data and high traffic loads.\n" +
                "- **Flexibility**: Easily customizable to fit specific use cases and requirements.\n" +
                "- **Open Source**: Foster rapid iteration and innovation through community collaboration.\n" +
                "\n" +
                "## Architecture\n" +
                "\n" +
                "The Fastx-AI LLM RAG System is built on a modular architecture, allowing for easy integration and extension. The core components include:\n" +
                "\n" +
                "- **Data Ingestion**: Collect and preprocess data from various sources.\n" +
                "- **Knowledge Base**: Store and manage structured and unstructured data.\n" +
                "- **Query Engine**: Process user queries and retrieve relevant information.\n" +
                "- **NLP Module**: Perform semantic analysis and natural language understanding.\n" +
                "- **API Layer**: Provide RESTful APIs for seamless integration with other systems.\n" +
                "\n" +
                "## Self Hosting\n" +
                "\n" +
                "We are excited for you to deploy our application! Below are the steps to get started:\n" +
                "\n" +
                "### Prerequisites\n" +
                "\n" +
                "- **MySQL**: Ensure you are using the latest version of MySQL, as the project utilizes features like `JSON` and `ENUM`.\n" +
                "- **Zookeeper**: Required for distributed coordination and configuration management.\n" +
                "- **Seata Server**: Required for distributed transactions.\n" +
                "- **Redis**: Used for caching and session management.\n" +
                "\n" +
                "**Note**: This is currently a development branch and should not be used in a production environment.\n" +
                "\n" +
                "## Dependencies\n" +
                "\n" +
                "The Fastx-AI LLM RAG System relies on several open-source libraries and frameworks:\n" +
                "\n" +
                "- **Dubbo/Sentinel**: For service governance and flow control.\n" +
                "- **MyBatis/Plus/P6spy**: For database operations and SQL logging.\n" +
                "- **Spring/Boot**: For building the application framework.\n" +
                "- **Zookeeper**: For distributed coordination.\n" +
                "- **Redis/Redisson**: For caching and distributed locks.\n" +
                "- **Seata**: For distributed transactions.\n" +
                "- **AutoService/Annotation**: For code generation and annotations.\n" +
                "\n" +
                "## Contributing\n" +
                "\n" +
                "We welcome contributions from developers of all backgrounds to enhance this essential component of future AI applications by sharing code and expertise. To contribute, please follow these steps:\n" +
                "\n" +
                "1. **Fork the Repository**: Fork the repository on GitHub.\n" +
                "2. **Create a Branch**: Create a new branch for your feature or bug fix.\n" +
                "3. **Submit a Pull Request**: Once your changes are ready, submit a pull request for review.\n" +
                "\n" +
                "## Thanks For Open Sources\n" +
                "\n" +
                "We would like to extend our gratitude to the following projects for their invaluable contributions:\n" +
                "\n" +
                "- **Dubbo/Sentinel**\n" +
                "- **MyBatis/Plus/P6spy**\n" +
                "- **Spring/Boot**\n" +
                "- **Zookeeper**\n" +
                "- **Redis/Redisson**\n" +
                "- **Seata**\n" +
                "- **AutoService/Annotation**\n" +
                "\n" +
                "Join us and let the Fastx-AI LLM RAG System become the cornerstone of your next intelligent application, helping to elevate AI technology to new heights.\n" +
                "\n" +
                "## API Reference\n" +
                "\n" +
                "<details>\n" +
                "<summary>The Fastx-AI LLM RAG System provides a RESTful API for seamless integration with other systems. The API endpoints are documented using Swagger, allowing developers to easily explore and interact with the system.</summary>\n" +
                "\n" +
                "```shell\n" +
                "###\n" +
                "GET http://localhost:8082/info/all\n" +
                "\n" +
                "###\n" +
                "POST http://localhost:8082/auth/user/createWithEmail\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "{\n" +
                "  \"email\": \"x.stark.dylan@gmail.com\",\n" +
                "  \"password\": \"123456\",\n" +
                "  \"username\": \"starkdylan\",\n" +
                "  \"profileImageUrl\": \"\"\n" +
                "}\n" +
                "\n" +
                "###\n" +
                "POST http://localhost:8082/auth/user/loginWithEmail\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "{\n" +
                "  \"email\": \"x.stark.dylan@gmail.com\",\n" +
                "  \"password\": \"e10adc3949ba59abbe56e057f20f883e\"\n" +
                "}\n" +
                "\n" +
                "###\n" +
                "POST http://localhost:8082/tool/platform/tool/exec\n" +
                "satoken: x.y.z.jwt.token generate by login or auth\n" +
                "Content-Type: application/json\n" +
                "\n" +
                "{\n" +
                "  \"toolCode\": \"openai.chat\",\n" +
                "  \"toolVersion\": \"1.0.0\",\n" +
                "  \"type\": \"llm-model\",\n" +
                "  \"input\": {\n" +
                "    \"config\": {\n" +
                "      \"apiKey\": \"sk-\",\n" +
                "      \"baseUrl\": \"https://x.com/v1\"\n" +
                "    },\n" +
                "    \"data\": {\n" +
                "      \"modelId\": \"gpt-4o-mini\",\n" +
                "      \"messages\": [\n" +
                "        {\n" +
                "          \"role\": \"user\",\n" +
                "          \"content\": \"hi\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"role\": \"system\",\n" +
                "          \"content\": \"please answer in Chinese\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "```\n" +
                "</details>\n" +
                "\n" +
                "## Others\n" +
                "\n" +
                "#### 1. if you meet git ignored file not worked, please try\n" +
                "\n" +
                "```shell\n" +
                "git rm -r --cached .\n" +
                "git add .\n" +
                "```\n" +
                "\n" +
                "#### 2. now we only support storage with S3 protocol, so you need to config your minio or s3 server with the following:\n" +
                "\n" +
                "```yaml\n" +
                "file:\n" +
                "  oss:\n" +
                "    domain: https://\n" +
                "    endpoint: https://\n" +
                "    bucket: \n" +
                "    region: \n" +
                "    access-key: 1\n" +
                "    access-secret: 2\n" +
                "```\n" +
                "\n" +
                "and modified `web/src/main/resources/application-upload-example.yaml` to `application-upload.yaml`.\n" +
                "\n" +
                "#### 3.about JAVA call python\n" +
                "\n" +
                "you should follow this steps to setup env first.\n" +
                "\n" +
                "```shell\n" +
                "1. open `knowledge-base-embedding` folder\n" +
                "2. create a conda enviroment use: conda create --name kbe python==3.12\n" +
                "3. pip install -r requirements.txt\n" +
                "4. open `platform-tool` folder, change workdir to `src/main/resources/python-script`\n" +
                "5. run: python image2vec.py \"http://xxx.png\"  to check image2vec\n" +
                "6. run: python text2vec.py \"Hello World\"  to check text2vec\n" +
                "7. run: python pdf2md.py \"http://xxx.pdf\" to check pdf2markdown\n" +
                "```\n" +
                "\n" +
                "> noticed that we use `/opt/anaconda3/envs/kbe/bin/paddleocr` in `pdf2md.py`, if your paddleocr executable file not in this path, you should change this path in `pdf2md.py` manually.  \n", "chunkSize", "200")));
        ChunkingProcessTool tool = new ChunkingProcessTool();
        System.out.println(JSON.toJSONString(tool.exec(input)));
    }
}
