const express = require('express');
const { createLevel, getLevels, deleteLevel } = require('./handler');
const router = express.Router();

router.post('/', createLevel);
router.get('/:id', getLevels);
router.delete('/:id', deleteLevel);

module.exports = router;
