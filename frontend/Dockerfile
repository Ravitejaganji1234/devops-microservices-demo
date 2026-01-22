FROM nginx:alpine

WORKDIR /app

COPY dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
