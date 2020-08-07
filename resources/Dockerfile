FROM node:latest

RUN mkdir -p /src/app
WORKDIR /src/app

RUN npm install --global http-server
COPY public /src/app

EXPOSE 80

CMD [ "http-server -p 80" ]