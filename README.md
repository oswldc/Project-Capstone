Fix issue in Bucket Storage

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
      const fileName=  `users/${uid}-${file.originalname}`
      const blob = bucket.file(fileName); // Path updated
      const blobStream = blob.createWriteStream({
        resumable: false,
        contentType: file.mimetype,
        public: true,
      });

      await new Promise((resolve, reject) => {
        blobStream.on('finish', () => {
          photoURL = `https://storage.googleapis.com/${bucketName}/${fileName}`; // Path updated
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
