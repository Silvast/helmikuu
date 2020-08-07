cd ~/helmikuu/resources
docker stop helmikuu || true && docker rm helmikuu || true
docker build --no-cache -t helmikuuimage .
docker run -d --rm --name helmikuu -p 8080:8080 helmikuuimage