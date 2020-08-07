cd ~/helmikuu/resources/configuration/nginx/
docker stop helmikuu || true && docker rm helmikuu || true
docker build --no-cache -t helmikuuimage .
docker run -d --rm -v ~/letsencrypt:/etc/letsencrypt --name helmikuu-nginx -p 80:80 helmikuuimage
#cd ~/helmikuu/resources/configuration/nginx
#docker build -t nginx-certbot .
#docker run -v $(pwd)/letsencrypt:/etc/letsencrypt --name nginx -ti -p 8080:80 nginx-cert sh
