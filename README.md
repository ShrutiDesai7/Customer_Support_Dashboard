# Customer Support Management System (Support Desk)

A full-stack cloud-based customer support platform where customers can raise support tickets and agents can manage and resolve them.

## Project Features

- **Customer Features**
  - Create support tickets with title and description
  - View ticket status and history
  - Communicate with support agents
  - Track ticket priority and updates

- **Agent Features**
  - View assigned tickets
  - Reply to tickets
  - Change ticket status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
  - Set ticket priority (LOW, MEDIUM, HIGH, URGENT)
  - Add internal notes (visible only to agents)
  - Auto-assignment logic for tickets

- **Core Features**
  - Ticket lifecycle management
  - Real-time messaging
  - Role-based access (CUSTOMER, AGENT, ADMIN)
  - User authentication and registration
  - Responsive UI

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MySQL or PostgreSQL
- **ORM**: JPA/Hibernate
- **Build**: Maven

### Frontend
- **Library**: React 18
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Build Tool**: Vite
- **Styling**: CSS

### API Communication
- REST APIs with JSON
- CORS enabled for frontend-backend communication

## Project Structure

```
zendesk/
├── backend/
│   ├── src/main/java/com/supportdesk/
│   │   ├── model/              # Entity classes (User, Ticket, Message)
│   │   ├── repository/         # JPA repositories
│   │   ├── service/            # Business logic
│   │   ├── controller/         # REST controllers
│   │   ├── config/             # Configuration classes
│   │   └── SupportDeskApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── pages/              # Full-page components
    │   ├── components/         # Reusable UI components
    │   ├── services/           # API integration
    │   ├── styles/             # CSS files
    │   ├── App.jsx
    │   ├── main.jsx
    │   └── index.css
    ├── index.html
    ├── package.json
    ├── vite.config.js
    └── .gitignore
```

## Installation & Setup

### Backend Setup

#### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 or PostgreSQL 12+

#### Steps

1. **Navigate to backend directory**
   ```bash
   cd backend
   ```

2. **Configure Database**
   - Edit `src/main/resources/application.properties`
   - For MySQL (default):
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/supportdesk_db
     spring.datasource.username=root
     spring.datasource.password=your_password
     ```
   - For PostgreSQL (uncomment in application.properties):
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/supportdesk_db
     spring.datasource.username=postgres
     spring.datasource.password=your_password
     ```

3. **Create Database**
   ```sql
   -- For MySQL
   CREATE DATABASE supportdesk_db;
   ```
   ```sql
   -- For PostgreSQL
   CREATE DATABASE supportdesk_db;
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   
   Backend will start at: `http://localhost:8080/api`

### Frontend Setup

#### Prerequisites
- Node.js 16+ and npm

#### Steps

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Start Development Server**
   ```bash
   npm run dev
   ```
   
   Frontend will start at: `http://localhost:5173`

## API Endpoints

### User Endpoints
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users/{id}` - Get user details
- `GET /api/users/agents` - Get all active agents
- `PUT /api/users/{id}` - Update user profile
- `DELETE /api/users/{id}` - Deactivate user

### Ticket Endpoints
- `POST /api/tickets` - Create new ticket
- `GET /api/tickets/{id}` - Get ticket details
- `GET /api/tickets/customer/{customerId}` - Get customer's tickets
- `GET /api/tickets/agent/{agentId}` - Get agent's assigned tickets
- `GET /api/tickets/unassigned` - Get unassigned tickets
- `GET /api/tickets` - Get all tickets
- `PATCH /api/tickets/{id}/status` - Update ticket status
- `PATCH /api/tickets/{id}/priority` - Update ticket priority
- `PATCH /api/tickets/{id}/assign` - Assign ticket to agent
- `PATCH /api/tickets/{id}/unassign` - Unassign ticket

### Message Endpoints
- `POST /api/messages/reply` - Add reply to ticket
- `POST /api/messages/note` - Add internal note
- `GET /api/messages/ticket/{ticketId}` - Get ticket messages
- `GET /api/messages/user/{userId}` - Get user's messages
- `GET /api/messages/{id}` - Get message details
- `DELETE /api/messages/{id}` - Delete message

## Usage

### As a Customer

1. Register as a CUSTOMER
2. Login with your credentials
3. Click "Create New Ticket" on the dashboard
4. Fill in the ticket details (title, description, priority)
5. View your tickets and their status
6. Click on any ticket to view details and send replies

### As an Agent

1. Register as an AGENT (or be registered by admin)
2. Login with your credentials
3. View assigned tickets in "Assigned to Me" tab
4. View unassigned tickets in "Unassigned" tab
5. Click on a ticket to open it
6. Reply to customers, change status/priority
7. Add internal notes (visible only to agents)
8. Assign/unassign tickets

## Database Schema

### Users Table
- Stores user information (customers, agents, admins)
- Columns: id, email, password, firstName, lastName, role, active, createdAt, updatedAt

### Tickets Table
- Stores support tickets
- Columns: id, title, description, status, priority, customerId, agentId, createdAt, updatedAt

### Messages Table
- Stores messages and notes
- Columns: id, content, ticketId, senderId, messageType, createdAt

## Security Considerations

**Note**: This is a beginner-friendly project. For production, implement:
- Password hashing using BCrypt
- JWT token-based authentication
- HTTPS/SSL encryption
- Input validation and sanitization
- SQL injection prevention
- CSRF protection
- Rate limiting
- Role-based access control (RBAC) with authorization filters

## Future Enhancements

- Email notifications for ticket updates
- File attachment support
- Advanced search and filtering
- SLA (Service Level Agreement) tracking
- Ticket templates
- Knowledge base integration
- Real-time notifications using WebSockets
- Admin dashboard with analytics
- Multi-language support
- User activity logging
- Ticket escalation workflows

## Development Tips

1. **Backend Debug**: Check logs in `target/logs` directory
2. **Frontend Debug**: Use React Developer Tools browser extension
3. **API Testing**: Use Postman or Thunderclient for testing endpoints
4. **Database**: Use MySQL Workbench or pgAdmin for database management

## Common Issues & Solutions

### Backend won't start
- Ensure database is running
- Check database credentials in `application.properties`
- Clear Maven cache: `mvn clean`

### Frontend won't load API
- Ensure backend is running on `http://localhost:8080`
- Check CORS configuration in `WebConfig.java`
- Check browser console for error messages

### Database connection issues
- Verify database server is running
- Check username and password
- Ensure database name matches in connection string

## Testing

### Sample Test Users

**Customer:**
- Email: customer@test.com
- Password: password123
- Role: CUSTOMER

**Agent:**
- Email: agent@test.com
- Password: password123
- Role: AGENT

## License

MIT License - Feel free to use this project for learning and development.

## Support

For questions or issues, refer to the code comments and the project structure documentation.
