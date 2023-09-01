# Stage 1: Build Angular Application
# Use the Node.js 18 image as the base image and name it 'angular_build'
FROM node:18 as angular_build
# Set the working directory inside the container to /app
WORKDIR /app
# Copy all the files from the local directory into the container's /app directory
COPY . /app/
RUN npm install
RUN npm run build

# Stage 2: Serve Angular Application using Nginx
# Use the nginx 1.16-alpine image as the base image
FROM nginx:1.16-alpine
# Copy the built files from the 'angular_build' stage into the Nginx's default web server directory
COPY --from=angular_build /app/dist/* /usr/share/nginx/html
# Copy the custom Nginx configuration file to the container's Nginx configuration directory
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Start Nginx with the configured settings
CMD ["nginx", "-g", "daemon off;"]
