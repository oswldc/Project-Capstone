require('dotenv').config();
const express = require('express');
const app = express();
const bodyParser = require('body-parser');

app.use(bodyParser.json());

// Routes
const userRoutes = require('./routes/users');
const levelRoutes = require('./routes/levels');
const exerciseRoutes = require('./routes/exercises');
const mediaRoutes = require('./routes/media');
const dictionaryRoutes = require('./routes/dictionary');
const resultRoutes = require('./routes/results');

app.use('/api/users', userRoutes);
app.use('/api/levels', levelRoutes);
app.use('/api/exercises', exerciseRoutes);
app.use('/api/media', mediaRoutes);
app.use('/api/dictionary', dictionaryRoutes);
app.use('/api/results', resultRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
