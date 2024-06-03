const express = require('express');
const { createUser, getUser, putUser, deleteUser } = require('./handler');
const router = express.Router();

router.post('/', createUser);
router.get('/:uid', getUser);
router.put('/:uid', putUser);
router.delete('/:uid', deleteUser);

module.exports = router;
