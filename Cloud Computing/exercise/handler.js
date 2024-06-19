const { db, bucket } = require('../config/db');
const { nanoid } = require('nanoid');

const createExercise = async (req, res) => {
  const { levelId} = req.body;
  const idExercise = nanoid(5);
  const { question } = req.file;
  try {
    await db.collection('exercises').doc(id).set({ levelId, question });
    res.status(201).send('Exercise created successfully');
  } catch (error) {
    res.status(500).send(error.message);
  }
};

const getExercise = async (req, res) => {
  const { levelId } = req.params;
  try {
    const exercises = await db.collection('exercises').where('levelId', '==', levelId).get();
    const exerciseList = exercises.docs.map(doc => doc.data());
    res.status(200).send(exerciseList);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

module.exports = { createExercise, getExercise };
