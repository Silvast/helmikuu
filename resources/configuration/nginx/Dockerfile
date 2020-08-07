FROM nginx:alpine
RUN apk add python3 python3-dev build-base libressl-dev musl-dev libffi-dev
RUN pip3 install pip --upgrade
RUN pip3 install certbot-nginx
RUN mkdir /etc/letsencrypt
COPY default.conf /etc/nginx/conf.d/default.conf
RUN npm install --global http-server
COPY public/ /usr/share/nginx/
EXPOSE 80