const { db } = require('../config/db');

const saveResult = async (req, res) => {
  const { userId, exerciseId, score } = req.body;
  try {
    await db.collection('results').add({ userId, exerciseId, score });
    res.status(201).send('Result saved successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { saveResult };
