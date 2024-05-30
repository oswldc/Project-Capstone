const express = require('express');
const { createLevel, getLevels } = require('./handler');
const router = express.Router();

router.post('/', createLevel);
router.get('/', getLevels);

module.exports = router;
