# Stage 1: Build Angular application

# Use official node image with Node 18 as the base image
FROM node:18-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the source code to the working directory
COPY package*.json ./
RUN npm install --legacy-peer-deps

# Copy the rest of the application files
COPY . .

# Build the Angular application
RUN npm run build

# Stage 2: Serve the application with Nginx

# Use official nginx image as the base image
FROM nginx:latest

# Set the environment variable for the profile (default to prod)
ARG PROFILE=localhost
ENV PROFILE=${PROFILE}
RUN mkdir -p /etc/nginx/ssl


# Copy the built output from the build stage
COPY --from=build /app/dist/sakai-ng /usr/share/nginx/html


# Copy the Nginx configuration and SSL certificates based on the profile
COPY src/zyn/nginx/${PROFILE}/nginx.conf /etc/nginx/nginx.conf
COPY src/zyn/nginx/${PROFILE}/default.conf /etc/nginx/conf.d/default.conf

COPY src/zyn/ssl/${PROFILE}/cert.pem /etc/nginx/ssl/cert.pem
COPY src/zyn/ssl/${PROFILE}/key.pem /etc/nginx/ssl/key.pem

EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
