FROM node:latest

RUN mkdir -p /src/app
WORKDIR /src/app

RUN npm install --global http-server
COPY public /src/app

EXPOSE 8080

CMD [ "http-server" ]