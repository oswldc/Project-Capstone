const { db } = require('../config/db');

const createUser = async (req, res) => {
  const { uid, name, email } = req.body;
  try {
    await db.collection('users').doc(uid).set({ name, email });
    res.status(201).send('User created successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getUser = async (req, res) => {
  const { uid } = req.params;
  try {
    const user = await db.collection('users').doc(uid).get();
    if (user.exists) {
      res.status(200).send(user.data());
    } else {
      res.status(404).send('User not found');
    }
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { createUser, getUser };
