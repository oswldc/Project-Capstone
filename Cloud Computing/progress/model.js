const { db } = require('../config/db');

class Progress {
  constructor(userId, level, completed) {
    this.userId = userId;
    this.level = level;
    this.completed = completed;
  }

  static async getProgress(userId) {
    const progressRef = db.collection('users').doc(userId).collection('progress');
    const snapshot = await progressRef.get();
    let progress = [];
    snapshot.forEach(doc => {
      progress.push(doc.data());
    });
    return progress;
  }

  static async setProgress(userId, level, completed) {
    const progressRef = db.collection('users').doc(userId).collection('progress').doc(level.toString());
    await progressRef.set({ level, completed });
    return new Progress(userId, level, completed);
  }
}

module.exports = Progress;
