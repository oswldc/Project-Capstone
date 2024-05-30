const { db } = require('../config/db');

const createLevel = async (req, res) => {
  const { id, description } = req.body;
  try {
    await db.collection('levels').doc(id).set({ description });
    res.status(201).send('Level created successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getLevels = async (req, res) => {
  try {
    const levels = await db.collection('levels').get();
    const levelsList = levels.docs.map(doc => doc.data());
    res.status(200).send(levelsList);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { createLevel, getLevels };
