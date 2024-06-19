const express = require('express');
const multer = require('multer');
const { uploadMedia } = require('./handler');
const router = express.Router();
const upload = multer({ storage: multer.memoryStorage() });

router.post('/upload', upload.single('file'), uploadMedia);

module.exports = router;
