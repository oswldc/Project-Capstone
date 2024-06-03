const { request } = require('express');
const { db } = require('../config/db');
const { nanoid } = require('nanoid');

const uid = nanoid(10);
const CreateAt = new Date().toISOString();
const UpdateAt = CreateAt;
const createUser = async (req, res) => {
  const { name, 
    email,
    password,
    photo,

   } = req.body;

  try {
    await db.collection('users').doc(uid).set({ name, email, password, photo, CreateAt, UpdateAt});
    res.status(201).send(`User created successfully with ID: ${uid}`);
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

const putUser = async (req, res) => {
  const { uid } = req.params;
  const { name, email, password, photo} = req.body;
  const updatedAt = new Date().toISOString();
  
  try {
    // Perbarui dokumen pengguna dengan nilai baru
    await db.collection('users').doc(uid).set({ name, email, password, photo, updatedAt }, { merge: true });
    res.status(200).send('User updated successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const deleteUser = async (req, res) => {
  const { uid } = req.params;
  try {
    await db.collection('users').doc(uid).delete();
    res.status(200).send('User deleted successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
}
module.exports = { createUser, getUser, putUser, deleteUser};
