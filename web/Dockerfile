FROM nginx:alpine
RUN rm -Rf /usr/share/nginx/html/*
COPY ./public /usr/share/nginx/html
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
