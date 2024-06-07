const express = require('express');
const { saveResultByUserId, getResultByUserId } = require('./handler');
const router = express.Router();

// Endpoint untuk menyimpan hasil berdasarkan ID Pengguna
router.post('/:userId', saveResultByUserId);

// Endpoint untuk mendapatkan hasil berdasarkan ID Pengguna
router.get('/:userId', getResultByUserId);

module.exports = router;
