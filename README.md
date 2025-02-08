<!DOCTYPE html>
<html>
<body>
<h1 align="center">🚀 Task Management API</h1>
<p align="center">A robust Spring Boot REST API for managing tasks with full CRUD operations and advanced status tracking</p>

<div align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.2-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-21-orange" alt="Java 17">
  <img src="https://img.shields.io/badge/JPA-Hibernate-blue" alt="JPA Hibernate">
  <img src="https://img.shields.io/badge/Testing-JUnit5%2BMockito-success" alt="Testing">
</div>

<h2>📌 Features</h2>
<ul>
  <li>✅ Full CRUD operations for tasks</li>
  <li>🔍 Advanced filtering by task status (PENDING, PROCESSING, COMPLETED)</li>
  <li>🛡 Comprehensive input validation and error handling</li>
  <li>📊 Automatic ID generation with JPA</li>
  <li>⚡ RESTful design with proper HTTP status codes</li>
  <li>📝 Detailed API documentation</li>
</ul>

<h2>🛠 Technologies</h2>
<ul>
  <li>Spring Boot 3.4.2</li>
  <li>Spring Data JPA</li>
  <li>Hibernate Validator</li>
  <li>H2 Database (in-memory)</li>
  <li>Maven Build System</li>
  <li>JUnit 5 & Mockito</li>
</ul>

<h2>🚀 Getting Started</h2>
<pre><code># Clone repository
git clone https://github.com/wistrum/taskfetch.api.git

# Build project
mvn clean install

# Run application
mvn spring-boot:run</code></pre>

<h2>📚 API Documentation</h2>
<h3>Endpoints Overview</h3>
<table>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/tasks</td>
    <td>Get all tasks</td>
  </tr>
  <tr>
    <td>GET</td>
    <td>/tasks/{id}</td>
    <td>Get task by ID</td>
  </tr>
  <tr>
    <td>POST</td>
    <td>/tasks</td>
    <td>Create new task</td>
  </tr>
  <tr>
    <td>PUT</td>
    <td>/tasks/{id}</td>
    <td>Update existing task</td>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/tasks/{id}</td>
    <td>Delete task</td>
  </tr>
  <tr>
    <td>GET</td>
    <td>/tasks/status</td>
    <td>Filter tasks by status</td>
  </tr>
</table>

<h3>📦 Example Request Body</h3>
<pre><code>{
  "title": "API Documentation",
  "description": "Create detailed API documentation",
  "status": "PENDING"
}</code></pre>

<h3>✅ Successful Response</h3>
<pre><code>HTTP/1.1 201 Created
{
  "id": 1,
  "title": "API Documentation",
  "description": "Create detailed API documentation",
  "status": "PENDING"
}</code></pre>

<h3>❌ Error Handling</h3>
<pre><code>HTTP/1.1 400 Bad Request
{
  "error": "Validation Failed: title - must not be blank"
}</code></pre>

<h2>🧪 Testing</h2>
<p>Comprehensive test suite with 90%+ coverage including:</p>
<ul>
  <li>✅ Controller endpoint validation</li>
  <li>✅ Error handling scenarios</li>
  <li>✅ Edge case testing</li>
  <li>✅ Mock service layer testing</li>
</ul>
<pre><code>mvn test</code></pre>

<h2>📜 License</h2>
<p>This project is licensed under the MIT License - see the <a href="LICENSE">LICENSE</a> file for details.</p>

<div align="center">
  <p>✨ Contributions welcome! Please open an issue or PR for any improvements</p>
</div>
</body>
</html>
