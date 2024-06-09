const { db } = require('../config/db');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const upload = require('../multer/multer');

// Pastikan Anda sudah mengatur autentikasi Google Cloud dengan benar
const storage = new Storage();
const bucketName = 'gencarabucket';
const bucket = storage.bucket(bucketName);

const addSign = async (req, res) => {
  const { word, description } = req.body;
  const file = req.file;
  const idDictionary = nanoid(6);
  

  try {
    let photoURL = null;
    if (file) {
      console.log('Uploading file:', file.originalname);
      const blob = bucket.file(`${idDictionary}/${file.originalname}`);
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

    await db.collection('dictionary').doc(idDictionary).set({
      word, description, imageUrl: photoURL
    });
    console.log('Dictionary created successfully with ID:', idDictionary);
    res.status(201).send(`Dictionary created successfully with ID: ${idDictionary}`);
  } catch (error) {
    console.error('Error creating user:', error.message);
    res.status(500).send(error.message);
  }
};


const getSigns = async (req, res) => {
  try {
    const signs = await db.collection('dictionary').get();
    const signsList = signs.docs.map(doc => doc.data());
    res.status(200).send(signsList);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getSignsbyId = async (req, res) => {
  const { idDictionary } = req.params;
  try {
    const signs = await db.collection('dictionary').doc(idDictionary).get();
    const signsList = signs.data();
    res.status(200).send(signsList);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { 
  addSign: [upload.single('imageUrl'), addSign], 
  getSigns, 
  getSignsbyId 
};
