const db = require('../config/db');

class User {
  static create(data) {
    return db.query('INSERT INTO Users (username, email, password, profile_photo) VALUES (?, ?, ?, ?)', [data.username, data.email, data.password, data.profile_photo]);
  }

  static findById(id) {
    return db.query('SELECT * FROM Users WHERE user_id = ?', [id]);
  }

  static findAll() {
    return db.query('SELECT * FROM Users');
  }

  static update(id, data) {
    return db.query('UPDATE Users SET username = ?, email = ?, password = ?, profile_photo = ? WHERE user_id = ?', [data.username, data.email, data.password, data.profile_photo, id]);
  }

  static delete(id) {
    return db.query('DELETE FROM Users WHERE user_id = ?', [id]);
  }
}

module.exports = User;
