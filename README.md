# Real-Time Stock Price Monitoring System
A real-time stock price monitoring system built with **Java**, **Spring Boot**, **Kafka**, **AWS MSK**, and **Docker**.

---

## **Project Overview**
This project is a real-time stock price monitoring system that:
- Accepts stock price updates via a REST API
- Publishes the updates to **Kafka**
- Consumes the updates from Kafka
- Stores the last 10 stock price updates in memory
- Integrates with **AWS MSK** (Managed Streaming for Apache Kafka)
- Runs as a **Docker container**

---

## **Project Structure**
```
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── stockmonitor
    │               ├── StockMonitorApplication.java
    │               ├── config
    │               │   ├── KafkaProducerConfig.java
    │               │   └── KafkaConsumerConfig.java
    │               ├── model
    │               │   └── StockPrice.java
    │               ├── producer
    │               │   └── StockPriceProducer.java
    │               ├── consumer
    │               │   └── StockPriceConsumer.java
    │               └── controller
    │                   └── StockPriceController.java
    └── resources
        └── application.properties
```

---

## **Tech Stack**
| Technology | Purpose |
|------------|---------|
| **Java 17** | Programming language |
| **Spring Boot** | REST API, Kafka integration |
| **Kafka** | Messaging system for real-time updates |
| **AWS MSK** | Managed Kafka service |
| **Docker** | Containerization |
| **Maven** | Dependency and build management |

---

## **Setup Instructions**

### **1. Clone the Repository**
```bash
git clone https://github.com/your-username/stock-monitor.git
cd stock-monitor
```

---

### **2. Set Up Kafka (AWS MSK)**
1. Go to **AWS Console → MSK**
2. Create a Kafka Cluster:
    - Cluster name → `stock-monitor-cluster`
    - Kafka version → `3.4.0`
    - Number of brokers → `3`
    - Instance type → `kafka.t3.small`
3. Get the Bootstrap Server from the MSK details page

---

### **3. Configure Kafka Settings**
Open `src/main/resources/application.properties` and update the Kafka configuration:

```properties
# AWS MSK Configuration
spring.kafka.bootstrap-servers=b-1.stock-monitor-cluster.xyz.kafka.us-east-1.amazonaws.com:9092
spring.kafka.consumer.group-id=stock-group
spring.kafka.consumer.auto-offset-reset=earliest

# AWS MSK-specific security settings
spring.kafka.properties.security.protocol=SASL_SSL
spring.kafka.properties.sasl.mechanism=AWS_MSK_IAM
spring.kafka.properties.sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;
spring.kafka.properties.sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler;
```

---

### ✅ **4. Set Up AWS Credentials**
Install the AWS CLI:

```bash
brew install awscli
```

Configure AWS credentials:

```bash
aws configure
```

Set up your credentials:
```
AWS Access Key ID [None]: <YOUR-ACCESS-KEY>
AWS Secret Access Key [None]: <YOUR-SECRET-KEY>
Default region name [None]: us-east-1
Default output format [None]: json
```

---

### ✅ **5. Build the Project with Maven**
```bash
./mvnw clean install
```

---

### ✅ **6. Build the Docker Image**
```bash
docker build --platform linux/arm64 -t stock-monitor .
```

---

### ✅ **7. Run the Docker Container**
```bash
docker run --platform linux/amd64 -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS="b-1.stock-monitor-cluster.xyz.kafka.us-east-1.amazonaws.com:9092" \
  -e SPRING_KAFKA_SECURITY_PROTOCOL="SASL_SSL" \
  -e SPRING_KAFKA_SASL_MECHANISM="AWS_MSK_IAM" \
  -e SPRING_KAFKA_SASL_JAAS_CONFIG="software.amazon.msk.auth.iam.IAMLoginModule required;" \
  -e SPRING_KAFKA_SASL_CALLBACK_HANDLER_CLASS="software.amazon.msk.auth.iam.IAMClientCallbackHandler" \
  stock-monitor
```

---

## **Endpoints**

### **Send Stock Price Update**
**POST** `/api/stocks/send`
- Sends a stock price to Kafka.

**Example Request:**
```bash
curl -X POST "http://localhost:8080/api/stocks/send?symbol=AAPL&price=180"
```

**Example Response:**
```json
"Stock price sent: StockPrice{symbol='AAPL', price=180, timestamp=2025-03-14T16:45:12}"
```

---

### **📈 Get Stock Price History**
**GET** `/api/stocks/history`
- Returns the last N stock price updates (default: 5).

**Example Request:**
```bash
curl "http://localhost:8080/api/stocks/history?limit=5"
```

**Example Response:**
```json
[
  {"symbol": "COMPANYA", "price": 180.00, "timestamp": "2025-03-14T16:45:12"},
  {"symbol": "COMPANYB", "price": 2900.00, "timestamp": "2025-03-14T16:45:32"}
]
```

---

## **Deploying to AWS Elastic Container Registry (ECR)**

### **1. Create an ECR Repository**
- Go to AWS Console → ECR
- Create a repository named `stock-monitor`

---

### **2. Authenticate Docker with AWS**
```bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <aws_account_id>.dkr.ecr.us-east-1.amazonaws.com
```

---

### **3. Tag and Push Docker Image**
Tag the Docker image:

```bash
docker tag stock-monitor:latest <aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/stock-monitor:latest
```

Push the Docker image:

```bash
docker push <aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/stock-monitor:latest
```

---

###  **4. Run the Container on AWS EC2**
SSH into your EC2 instance:

```bash
ssh -i <your-key>.pem ec2-user@<your-ec2-ip>
```

Pull the Docker image:

```bash
docker pull <aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/stock-monitor:latest
```

Run the container:

```bash
docker run -d -p 8080:8080 --env SPRING_PROFILES_ACTIVE=prod <aws_account_id>.dkr.ecr.us-east-1.amazonaws.com/stock-monitor:latest
```

