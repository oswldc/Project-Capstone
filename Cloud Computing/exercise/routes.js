const express = require('express');
const { createExercise, getExercise } = require('./handler');
const router = express.Router();

router.post('/', createExercise);
router.get('/:levelId', getExercise);

module.exports = router;
