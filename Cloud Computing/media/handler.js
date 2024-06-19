const { bucket } = require('../config/db');

const uploadMedia = async (req, res) => {
  const file = req.file;
  const fileName = `${Date.now()}_${file.originalname}`;
  const fileUpload = bucket.file(fileName);
  
  const stream = fileUpload.createWriteStream({
    metadata: {
      contentType: file.mimetype
    }
  });

  stream.on('error', (err) => {
    res.status(500).send(err.message);
  });

  stream.on('finish', () => {
    fileUpload.makePublic().then(() => {
      res.status(200).send(`File uploaded successfully. Public URL: ${fileUpload.publicUrl()}`);
    });
  });

  stream.end(file.buffer);
};

module.exports = { uploadMedia };
