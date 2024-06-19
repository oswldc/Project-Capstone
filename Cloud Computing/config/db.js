const admin = require('firebase-admin');
const serviceAccount = require('./ServiceAccountKeyGencaraApp.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  storageBucket: "gencarabucket"
});

const db = admin.firestore();
const bucket = admin.storage().bucket();

module.exports = { db, bucket };
