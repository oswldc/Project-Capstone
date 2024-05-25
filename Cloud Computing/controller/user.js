const User = require('../models/user');

exports.createUser = (req, res) => {
  User.create(req.body)
    .then(() => res.status(201).json({ message: 'User created successfully' }))
    .catch(err => res.status(500).json({ error: err.message }));
};

exports.getUserById = (req, res) => {
  User.findById(req.params.user_id)
    .then(([rows]) => {
      if (rows.length === 0) return res.status(404).json({ error: 'User not found' });
      res.status(200).json(rows[0]);
    })
    .catch(err => res.status(500).json({ error: err.message }));
};

exports.getAllUsers = (req, res) => {
  User.findAll()
    .then(([rows]) => res.status(200).json(rows))
    .catch(err => res.status(500).json({ error: err.message }));
};

exports.updateUser = (req, res) => {
  User.update(req.params.user_id, req.body)
    .then(() => res.status(200).json({ message: 'User updated successfully' }))
    .catch(err => res.status(500).json({ error: err.message }));
};

exports.deleteUser = (req, res) => {
  User.delete(req.params.user_id)
    .then(() => res.status(200).json({ message: 'User deleted successfully' }))
    .catch(err => res.status(500).json({ error: err.message }));
};
