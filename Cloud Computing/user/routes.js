const express = require('express');
const { createUser, getUser } = require('./handler');
const router = express.Router();

router.post('/', createUser);
router.get('/:uid', getUser);

module.exports = router;
