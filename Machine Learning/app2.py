from flask import Flask, request, jsonify
from keras.models import model_from_json
from PIL import Image
import numpy as np
import io
import base64

app = Flask(__name__)

with open("model.json", "r") as json_file:
    loaded_model_json = json_file.read()
loaded_model = model_from_json(loaded_model_json)
loaded_model.load_weights("model_weights.h5")
loaded_model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

label_map = {i: chr(65 + i) for i in range(26)}  

def preprocess_image(image, target_size):
    if image.mode != "RGB":
        image = image.convert("RGB")
    image = image.resize(target_size)
    image = np.array(image)
    image = np.expand_dims(image, axis=0)
    image = image / 255.0
    return image

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image provided'})
    
    image = request.files['image']
    image = Image.open(image)
    processed_image = preprocess_image(image, target_size=(64, 64))
    prediction = loaded_model.predict(processed_image).tolist()
    predicted_label_index = np.argmax(prediction, axis=1)[0]
    predicted_abjad = label_map[predicted_label_index]
    response = {
        'prediction': predicted_abjad
    }
    return jsonify(response)

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000, debug=True)
