
FROM node:18 as angular_build
WORKDIR /app
COPY . /app/
RUN npm install
RUN npm run build 


FROM nginx:latest
COPY --from=angular_build /app/dist/* /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]
