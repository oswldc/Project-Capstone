const Progress = require('../progress/model');

exports.getProgress = async (req, res) => {
  const { userId } = req.params;
  try {
    const progress = await Progress.getProgress(userId);
    res.status(200).json(progress);
  } catch (error) {
    res.status(500).send(error.message);
  }
};

exports.setProgress = async (req, res) => {
  const { userId, level } = req.body;
  try {
    const progress = await Progress.setProgress(userId, level, true);
    res.status(200).json(progress);
  } catch (error) {
    res.status(500).send(error.message);
  }
};
