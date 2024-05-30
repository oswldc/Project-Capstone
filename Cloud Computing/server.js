const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const port = process.env.PORT || 3000;

const userRoutes = require('./user/routes');
const levelRoutes = require('./level/routes');
const exerciseRoutes = require('./exercise/routes');
const mediaRoutes = require('./media/routes');
const dictionaryRoutes = require('./dictionary/routes');
const resultRoutes = require('./result/routes');

app.use(bodyParser.json());

app.use('/users', userRoutes);
app.use('/levels', levelRoutes);
app.use('/exercises', exerciseRoutes);
app.use('/media', mediaRoutes);
app.use('/dictionary', dictionaryRoutes);
app.use('/results', resultRoutes);

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
