const { db } = require('../config/db');

const addSign = async (req, res) => {
  const { id, word, description, imageUrl } = req.body;
  try {
    await db.collection('dictionary').doc(id).set({ word, description, imageUrl });
    res.status(201).send('Sign added successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getSigns = async (req, res) => {
  try {
    const signs = await db.collection('dictionary').get();
    const signsList = signs.docs.map(doc => doc.data());
    res.status(200).send(signsList);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { addSign, getSigns };
