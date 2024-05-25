const mysql = require('mysql2');

const pool = mysql.createPool({
  host: 'localhost',
  user: 'your_database_user',
  password: 'your_database_password',
  database: 'sign_language_learning',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

module.exports = pool.promise();
