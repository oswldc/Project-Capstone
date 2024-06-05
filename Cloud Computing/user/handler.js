// controllers/userController.js
const { db } = require('../config/db');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const upload = require('../multer/multer');

const storage = new Storage();
const bucketName = 'gencaraproject';
const bucket = storage.bucket(bucketName);


//Register
const createUser = async (req, res) => {
  const { name, email, password } = req.body;
  const file = req.file;
  const uid = nanoid(10);
  const CreateAt = new Date().toISOString();
  const UpdateAt = CreateAt;

  try {
    let photoURL = null;
    if (file) {
      const blob = bucket.file(`${uid}/${file.originalname}`);
      const blobStream = blob.createWriteStream({
        resumable: false,
        contentType: file.mimetype,
        public: true,
      });

      await new Promise((resolve, reject) => {
        blobStream.on('finish', () => {
          photoURL = `https://storage.googleapis.com/${bucketName}/${blob.name}`;
          resolve();
        });
        blobStream.on('error', (err) => reject(err));
        blobStream.end(file.buffer);
      });
    }

    await db.collection('users').doc(uid).set({ name, email, password, photo: photoURL, CreateAt, UpdateAt });
    res.status(201).send(`User created successfully with ID: ${uid}`);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

//Login



const putUser = async (req, res) => {
  const { uid } = req.params;
  const { name, email, password } = req.body;
  const file = req.file;
  const updatedAt = new Date().toISOString();
  
  try {
    let photoURL = null;
    if (file) {
      const blob = bucket.file(`${uid}/${file.originalname}`);
      const blobStream = blob.createWriteStream({
        resumable: false,
        contentType: file.mimetype,
        public: true,
      });

      await new Promise((resolve, reject) => {
        blobStream.on('finish', () => {
          photoURL = `https://storage.googleapis.com/${bucketName}/${blob.name}`;
          resolve();
        });
        blobStream.on('error', (err) => reject(err));
        blobStream.end(file.buffer);
      });
    }

    const updateData = { name, email, password, updatedAt };
    if (photoURL) {
      updateData.photo = photoURL;
    }

    await db.collection('users').doc(uid).set(updateData, { merge: true });
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

module.exports = { createUser: [upload.single('photo'), createUser], getUser, putUser: [upload.single('photo'), putUser], deleteUser };
