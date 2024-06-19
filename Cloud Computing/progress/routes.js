const express = require('express');
const { getProgress, setProgress } = require('./handler');
const router = express.Router();

router.get('/:userId', getProgress);
router.post('/', setProgress);

module.exports = router;
