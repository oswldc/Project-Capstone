const { db } = require('../config/db');
const { nanoid } = require('nanoid');
const { Result } = require('./model'); // Import Result = require('./Result');


const saveResultByUserId = async (req, res) => {
  const { userId } = req.params;
  let { score } = req.body;

  score = parseFloat(score);

  if (isNaN(score) || score === '') {
    return res.status(400).send('Nilai skor tidak valid');
  }

  try {
    const userSnapshot = await db.collection('users').doc(userId).get();

    if (!userSnapshot.exists) {
      return res.status(404).send('ID Pengguna tidak ditemukan');
    }

    const exerciseId = nanoid(5);

    // Membuat instance Result menggunakan constructor
    const result = new Result(exerciseId, userId, score);

    // Simpan hasil ke koleksi results
    await db.collection('results').add(result);

    res.status(201).send('Hasil berhasil disimpan');
  } catch (error) {
    res.status(500).send(error.message);
  }
};



const getResultByUserId = async (req, res) => {
  const { userId } = req.params;

  try {
    // Ambil hasil dari koleksi results berdasarkan ID Pengguna
    const resultsSnapshot = await db.collection('results').where('userId', '==', userId).get();

    if (resultsSnapshot.empty) {
      return res.status(404).send('Hasil tidak ditemukan untuk ID Pengguna ini');
    }

    const results = resultsSnapshot.docs.map(doc => doc.data());
    res.status(200).json(results);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { saveResultByUserId, getResultByUserId };
