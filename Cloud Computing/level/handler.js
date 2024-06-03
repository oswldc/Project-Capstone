const { db } = require('../config/db');
const { nanoid } = require('nanoid');


const createLevel = async (req, res) => {
  const { name, description } = req.body;
  const id = nanoid(5);
  try {
    await db.collection('levels').doc(id).set({ name, description });
    res.status(201).send('Level created successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getLevels = async (req, res) => {
  try {
    const levels = [];
    const querySnapshot = await db.collection('levels').get();
    querySnapshot.forEach((doc) => {
      levels.push({ id: doc.id, ...doc.data() });
    });
    res.status(200).send(levels);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const deleteLevel = async (req, res) => {
  const { id } = req.params;
  try {
    await db.collection('levels').doc(id).delete();
    res.status(200).send('Level deleted successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { createLevel, getLevels, deleteLevel };
