# Notification Service

## Introduction

The Notification Service is part of a microservices architecture designed to manage and send notifications based on events. This service interacts with various databases and other microservices to deliver a cohesive notification system, capable of handling contact uploads, event notifications, and SMS alerts.

## Requirements

Before you can run the Notification Service, ensure you have the following installed on your machine:

- **Docker**: Ensure that Docker is installed and running.
- **Docker Compose**: Required for orchestrating the multiple services.

## Environment Variables

You need to configure environment variables for your databases and services. Create a file named `.env` in the root directory of the project and add the following variables:

MYSQL_ROOT_PASSWORD=your_mysql_root_password
CONTACT_DB_PASSWORD=your_contact_db_password
EVENT_DB_PASSWORD=your_event_db_password
FILE_DB_PASSWORD=your_file_db_password
NOTIFICATION_DB_PASSWORD=your_notification_db_password
SMS_DB_PASSWORD=your_sms_db_password
USER_DB_PASSWORD=your_user_db_password
JWT_SECRET=your_jwt_secret
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=your_twilio_phone_number

Make sure to replace the placeholder values with your actual passwords and tokens.

Docker Compose Configuration
The project uses Docker Compose to manage the services. Below is a brief overview of the services defined in docker-compose.yml:

MySQL Databases: Separate databases for contacts, events, files, notifications, SMS, and users.
Microservices:
Contact Service: Manages contact information.
Event Service: Handles events triggering notifications.
File Service: Processes CSV file uploads for contacts.
Notification Service: Creates and manages notifications.
SMS Service: Sends SMS notifications using a provider like Twilio.
User Service: Manages user authentication and information.

Starting the Application
To run the Notification Service, follow these steps:

Clone the Repository: Open your terminal and clone the repository:

bash
Copy code
git clone https://github.com/Bolshov92/Notification_System
cd Notification_System
Create the .env File: Create a .env file in the root directory and configure the environment variables as described above.

Start the Services: Use Docker Compose to start the services:

bash
Copy code
docker-compose up -d
The -d flag runs the containers in detached mode.

Check the Status: You can check the status of the services by running:

bash
Copy code
docker-compose ps
View Logs: To view logs for all services, use:

bash
Copy code
docker-compose logs -f
This command will display the logs in real-time.

Authentication
To access the Notification Service, you'll need to authenticate as an admin. Use the following credentials:

Username: ADMIN
Password: admin
You can authenticate by making a POST request to the authentication endpoint:

http
Copy code
POST http://localhost:8086/api/users/authenticate
Content-Type: application/json

{
    "userName": "ADMIN",
    "password": "admin"
}
If the credentials are correct, you will receive a JWT token in response.

## Scheduling Notifications

To create a notification schedule, specify the following parameters in your request:

- **fileName**: The name of the uploaded file.
- **eventName**: The name of the event.
- **sendTime**: The scheduled time for sending notifications.

### Example Request

Here’s an example of a JSON request to schedule notifications:

   json
{
    "fileName": "contactS.csv",
    "eventName": "TEST",
    "sendTime": "2024-10-24T06:00:00Z"
}

SMS Provider Configuration
For SMS notifications, you will need to configure your SMS provider settings (e.g., Twilio) in the .env file:

plaintext
Copy code
TWILIO_ACCOUNT_SID=your_twilio_account_sid
TWILIO_AUTH_TOKEN=your_twilio_auth_token
TWILIO_PHONE_NUMBER=your_twilio_phone_number
Ports
Ensure to access each service through the respective ports:

Contact Service: 8081
Event Service: 8082
File Service: 8083
Notification Service: 8087
SMS Service: 8085
User Service: 8086

Conclusion
The Notification Service is a robust system for managing notifications through a microservices architecture. Ensure you have configured your environment variables correctly and follow the steps above to run the application successfully.

Contact Information
For any inquiries or support, please reach out to:

Name: Viacheslav
Email: bolshov92@gmail.com
GitHub: https://github.com/Bolshov92
LinkedIn: https://www.linkedin.com/in/viacheslav-bolshov-537621242/
