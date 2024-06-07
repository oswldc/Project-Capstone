const { db } = require('../config/db');
const { nanoid } = require('nanoid');


const createLevel = async (req, res) => {
  const { 
    name, 
    description 
  } = req.body;

  const idLevel = nanoid(5);
  try {
    await db.collection('levels').doc(idLevel).set({ name, description });
    res.status(201).send('Level created successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getLevels = async (req, res) => {
  const { idLevel } = req.params;
  try {
    const levelSnapshot = await db.collection('levels').doc(idLevel).get();

    if (!levelSnapshot.exists) {
      return res.status(404).send('Level tidak ditemukan');
    }

    const levelData = levelSnapshot.data();

    res.status(200).json(levelData);
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
