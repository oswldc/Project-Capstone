from flask import Flask, request, jsonify, render_template
from keras.models import model_from_json
from PIL import Image
import numpy as np
import io
import base64

app = Flask(__name__)

# Memuat model
with open("model.json", "r") as json_file:
    loaded_model_json = json_file.read()
loaded_model = model_from_json(loaded_model_json)
loaded_model.load_weights("model_weights.h5")
loaded_model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Map label indeks ke abjad
label_map = {i: chr(65 + i) for i in range(26)}  

def preprocess_image(image, target_size):
    if image.mode != "RGB":
        image = image.convert("RGB")
    image = image.resize(target_size)
    image = np.array(image)
    image = np.expand_dims(image, axis=0)
    image = image / 255.0
    return image

@app.route('/')
def index():
    return render_template('index.html')

@app.route("/predict", methods=["POST"])
def predict():
    message = request.get_json(force=True)
    encoded = message['image']
    decoded = base64.b64decode(encoded)
    image = Image.open(io.BytesIO(decoded))
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
