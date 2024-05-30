const express = require('express');
const { saveResult } = require('./handler');
const router = express.Router();

router.post('/', saveResult);

module.exports = router;
