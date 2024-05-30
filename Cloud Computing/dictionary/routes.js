const express = require('express');
const { addSign, getSigns } = require('./handler');
const router = express.Router();

router.post('/', addSign);
router.get('/', getSigns);

module.exports = router;
