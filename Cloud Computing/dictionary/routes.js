const express = require('express');
const { addSign, getSigns, getSignsbyId } = require('./handler');
const router = express.Router();

router.post('/', addSign);
router.get('/', getSigns);
router.get('/:id', getSignsbyId);

module.exports = router;
