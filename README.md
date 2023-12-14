# Energy Management System :zap:

## Overview :bulb:
This project focuses on developing an Energy Management System, designed to manage users and their smart energy metering devices. It caters to two user roles: administrators (managers) and clients, each with distinct access and functionalities. The system is built on a microservices architecture, enhancing the scalability and efficiency of managing energy data.

## Functionalities :gear:
### Administrator/Manager Role
- **User Management**: Perform CRUD operations on user accounts, including details like ID, name, and role (admin/client).
- **Device Management**: Handle CRUD operations for smart energy metering devices, defined by ID, description, address, and maximum hourly energy consumption.
- **User-Device Mapping**: Create associations between users and their smart devices: a user can be asssociated multiple devices, this implies synchronization between the two microservices
- **Support Chat**: Chat with mutliple clients and receive delivered/read notifications, as well as typing notifications.

### Client/User Role
- **Device Overview**: Clients can view all their devices on their respective pages.
- **Hourly energy consumption**: The hourly energy consumption can be viewed on a per-device basis in a user-friendly chart, alongside notifications when the maximum hourly energy consumption is exceeded.

### Access Control
- **Role-Based Access**: Ensures that users can only access pages corresponding to their role, preventing unauthorized access to admin functions.

## Implementation Technologies :computer:
- **Microservices**: Utilized Java Spring Boot for backend microservices, Spring Security, Hibernate, WebSocket, Indirect Communication using message queues. The device simulator uses a RabbitMQ queue to send measurement data to the monitoring and communication microservice.
- **Frontend**: JavaScript-based framework React for the frontend.
- **Security**: Implemented authentication methods like JWT authentication and Spring Security. All the microservices are protected against access.

## Monitoring and Communication Microservice :satellite:
- **Message Broker Middleware**: Collects data from smart metering devices, computes hourly energy consumption, and stores it in a database.
- **Smart Metering Device Simulator**: A standalone app that simulates a smart meter, sending data in JSON format to the message broker.
- **Event-Based Synchronization**: Uses a RabbitMQ queue for device changes to synchronize databases between Device Management and Monitoring Microservices.

## Chat Microservice :speech_balloon:
- **Chat Microservice**: Allows real-time communication between users and the system administrator, with the administrator being able to chat with multiple clients at the same time.
- **Read Notifications**: Only when the other user reads the message will the checkmarks turn blue.
- **Typing Notifications**: When the other user is typing, a notification will be displayed on the page that will expire when the other user stops typing.

## Deployment :cloud:
- **Docker**: Containerization of microservices for efficient deployment, each microservice containerized as a different service, grouped in a docker compose.
- **Azure Cloud**: Hosting services on the Azure platform using the Azure container registry. 
- **CI/CD Integration**: Implementing continuous integration and deployment pipeline using GitLab.


## GUI :computer: 
### User Management
![image](https://github.com/Jailor/energy-management-system/assets/44724573/c6850b08-5a5c-4175-bfc4-81586a39a5e6)
### Device Form
![image](https://github.com/Jailor/energy-management-system/assets/44724573/0f1d4479-f5bf-4443-b4ae-2cd9aea93dca)
### View Device Consumption
![image](https://github.com/Jailor/energy-management-system/assets/44724573/ea481274-54b7-48ec-9a42-00c68f65b70f)
### Chat
![image](https://github.com/Jailor/energy-management-system/assets/44724573/20749720-ebe0-41d0-aeba-71ef1a98d57a)
### Admin Chat
![image](https://github.com/Jailor/energy-management-system/assets/44724573/458432ba-a9b8-49ab-95d9-59d7460fb3d8)



