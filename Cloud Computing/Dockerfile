# Gunakan image node.js sebagai base image
FROM node:18

# Set working directory
WORKDIR /usr/src/app

# Salin package.json dan package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Salin seluruh kode sumber aplikasi ke dalam container
COPY . .

# Expose port aplikasi
EXPOSE 3000

# Set environment variable untuk production
ENV NODE_ENV=production

# Jalankan server aplikasi
CMD ["npm", "start"]
