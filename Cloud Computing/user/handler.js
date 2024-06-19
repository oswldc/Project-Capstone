const { db } = require('../config/db');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const upload = require('../multer/multer');

// Pastikan Anda sudah mengatur autentikasi Google Cloud dengan benar
const storage = new Storage();
const bucketName = 'gencarabucket';
const bucket = storage.bucket(bucketName);

const createUser = async (req, res) => {
  const { name, email, password } = req.body;
  const file = req.file;
  const uid = nanoid(10);
  const createAt = new Date().toISOString();
  const updateAt = createAt;

  try {
    let photoURL = null;
    if (file) {
      console.log('Uploading file:', file.originalname);
      const blob = bucket.file(`${uid}/${file.originalname}`);
      const blobStream = blob.createWriteStream({
        resumable: false,
        contentType: file.mimetype,
        public: true,
      });

      await new Promise((resolve, reject) => {
        blobStream.on('finish', () => {
          photoURL = `https://storage.googleapis.com/${bucketName}/${blob.name}`;
          console.log('File uploaded to:', photoURL);
          resolve();
        });
        blobStream.on('error', (err) => {
          console.error('Upload error:', err);
          reject(err);
        });
        blobStream.end(file.buffer);
      });
    }

    await db.collection('users').doc(uid).set({
      name, email, password, photo: photoURL, createAt, updateAt
    });
    console.log('User created successfully with ID:', uid);
    res.status(201).send(`User created successfully with ID: ${uid}`);
  } catch (error) {
    console.error('Error creating user:', error.message);
    res.status(500).send(error.message);
  }
};

const putUser = async (req, res) => {
  const { uid } = req.params;
  const { name, email, password } = req.body;
  const file = req.file;
  const updatedAt = new Date().toISOString();
  
  try {
    let photoURL = null;
    if (file) {
      console.log('Uploading file:', file.originalname);
      const blob = bucket.file(`${uid}/${file.originalname}`);
      const blobStream = blob.createWriteStream({
        resumable: false,
        contentType: file.mimetype,
        public: true,
      });

      await new Promise((resolve, reject) => {
        blobStream.on('finish', () => {
          photoURL = `https://storage.googleapis.com/${bucketName}/${blob.name}`;
          console.log('File uploaded to:', photoURL);
          resolve();
        });
        blobStream.on('error', (err) => {
          console.error('Upload error:', err);
          reject(err);
        });
        blobStream.end(file.buffer);
      });
    }

    const updateData = { name, email, password, updatedAt };
    if (photoURL) {
      updateData.photo = photoURL;
    }

    await db.collection('users').doc(uid).set(updateData, { merge: true });
    console.log('User updated successfully:', uid);
    res.status(200).send('User updated successfully');
  } catch (error) {
    console.error('Error updating user:', error.message);
    res.status(500).send(error.message);
  }
};

const deleteUser = async (req, res) => {
  const { uid } = req.params;
  try {
    await db.collection('users').doc(uid).delete();
    console.log('User deleted successfully:', uid);
    res.status(200).send('User deleted successfully');
  } catch (error) {
    console.error('Error deleting user:', error.message);
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
    console.error('Error getting user:', error.message);
    res.status(500).send(error.message);
  }
};

module.exports = { 
  createUser: [upload.single('photo'), createUser], 
  getUser, 
  putUser: [upload.single('photo'), putUser], 
  deleteUser 
};
